package com.example.ciklo.auth;

import com.example.ciklo.auth.email.EmailSender;
import com.example.ciklo.config.JwtService;
import com.example.ciklo.model.Admin;
import com.example.ciklo.model.ConfirmToken;
import com.example.ciklo.model.Customer;
import com.example.ciklo.model.Driver;
import com.example.ciklo.repository.AdminRepo;
import com.example.ciklo.repository.CustomerRepo;
import com.example.ciklo.repository.DriverRepo;
import com.example.ciklo.service.ConfirmToken.ConfirmTokenServiceImpl;
import com.example.ciklo.service.admin.AdminDetailServiceImpl;
import com.example.ciklo.service.customer.CustomerDetailServiceImpl;
import com.example.ciklo.service.driver.DriverDetailServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthenticationService {

    private final CustomerRepo cusRep;
    private final DriverRepo driverRep;
    private final AdminRepo adRep;
    private final PasswordEncoder encoder;
    private final JwtService service;
    private final AuthenticationManager manager;
    private final CustomerDetailServiceImpl customerDetailService;
    private final AdminDetailServiceImpl adminDetailService;
    private final DriverDetailServiceImpl driverDetailService;
    private final ConfirmTokenServiceImpl confirmTokenService;
    private final EmailSender emailSender;

    public AuthenticationResponse register(RegisterCusRequest request) throws Exception {
        var cus = Customer.builder()
                .cFirstname(request.getCFirstname())
                .cLastname(request.getCLastname())
                .CEmail(request.getCEmail())
                .cPassword(encoder.encode(request.getCPassword()))
                .cPhone(request.getCPhone())
                .enabled(false)
                .locked(false)
                .build();
        try {
            if (cusRep.findCustomerByCEmail(cus.getCEmail()).isPresent()) {
                log.error("Exist email {} in DB", cus.getCEmail());
                return null;
            } else {
                log.info("Register customer: {}", cus);
                cusRep.save(cus);

                String link = "http://localhost:8080/ciklo/auth/confirm?token=";

                // create confirm Token
                sendLinkConfirm(cus, "Thank you for registering. Please click on the below link to activate your account:", link);

                var jwtToken = service.generateToken(cus);
                var jwtRefresh = service.refreshToken(cus);

//                log.info("Get getAuthorities: {}", cus.getAuthorities());
//                log.info("Register Token access: {}", jwtToken);
//                log.info("Register Token refresh: {}", jwtRefresh);
                return new AuthenticationResponse(jwtToken, jwtRefresh);
            }
        } catch (Exception e) {
            log.error("Something wrong happen in register: {}", e.getMessage());
        }
        return null;
    }

    public void sendLinkConfirm(Customer cus, String msg, String link)
    {
        ConfirmToken token = createTokenConfirm(cus);
        confirmTokenService.saveConfirmToken(token);
        emailSender.send(cus.getCEmail(), buildEmail(cus.getCFirstname(), link + token.getToken(), msg));
    }

    public ConfirmToken createTokenConfirm(Customer cus) {

        return new ConfirmToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10),
                cus
        );
    }


    @Transactional
    public int confirmToken(String token) {
        ConfirmToken confirmToken = confirmTokenService
                .getConfirmToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("Token not found")
                );
        if (confirmToken.getConfirmAt() != null) {
//            log.info("Already confirm");
            return 1; //Already confirm
        }
        LocalDateTime expired = confirmToken.getExpireAt();
        if (expired.isBefore(LocalDateTime.now())) {
//            log.info("Token has already expired");
            return 2; //Token has already expired
        }

        confirmTokenService.setConfirmToken(token);
        setEnableCustomer(confirmToken.getCus().getCEmail());
        return 0;
    }

    private void setEnableCustomer(String email) {
        cusRep.enableAppUser(email);
    }

    //------------------------------------------------------------------------------------------
    //Send confirm reset password

    public void resetPassword(String email)
    {
        Customer cus = cusRep.findCustomerByCEmail(email).orElse(null);
        sendLinkConfirmPassword(cus);
    }

    private void sendLinkConfirmPassword(Customer cus)
    {
        ConfirmToken token = confirmTokenService.getConfirmTokenByCustomer(cus).orElse(null);
        if (token != null)
        {
            emailSender.send(cus.getCEmail(), buildEmail(cus.getCFirstname(), "http://localhost:8080/ciklo/auth/password_confirm?token=" + token.getToken(), "Click below link to reset password:"));
        }

    }

    @Transactional
    public int confirmPassword(String token) {
        ConfirmToken confirmToken = confirmTokenService
                .getConfirmToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("Token not found")
                );

        if (confirmToken.getConfirmAt() != null) {
//            log.info("Already confirm");
            return 1; //Already confirm -> reset password
        }
        else {
            return 0; //Account has not been active
        }
    }

    @Transactional
    public boolean updatePassword(String email, String password)
    {
        String pass =encoder.encode(password);
        try
        {
            cusRep.changePassword(email, pass);
            return true;
        }catch(Exception e)
        {
            log.error("error in change password -> " +  e.getMessage());
            return false;
        }
    }

    //-------------------------------------------------------------------------------------------
    public AuthenticationResponse authenticate(AuthenticateRequest request) {
/*//        manager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );*/

        int role = 0;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (cusRep.findCustomerByCEmail(request.getEmail()).isPresent()) {
            role = 1;
        } else if (driverRep.findByDEmail(request.getEmail()).isPresent()) {
            role = 2;
        } else if (adRep.findAdminByAdEmail(request.getEmail()).isPresent()) {
            role = 3;
        } else {
            throw new UsernameNotFoundException("NOT FOUND");
        }

        switch (role) {
            case 1:
                var cus = cusRep.findCustomerByCEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("NOT FOUND CUSTOMER"));

                if(cus.isEnabled())
                {
                    log.info("Login info customer {}", cus.getCEmail(), cus.getPassword());
                    return getAuthenticateResponse(cus, encoder, request, cus.getPassword());
                }
                else {
                    log.info("Account has not active");
                    return null;
                }

            case 2:
                var driver = driverRep.findByDEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("NOT FOUND DRIVER"));

                if (driver.isActive()) {
                    log.info("Login info driver {}", driver.getDEmail(), driver.getPassword());
                    return getAuthenticateResponse(driver, encoder, request, driver.getPassword());
                } else {
                    log.info("Driver account has been deleted");
                    return null;
                }

            case 3:
                var admin = adRep.findAdminByAdEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("NOT FOUND ADMIN"));
                log.info("Login info admin {}", admin.getAdEmail(), admin.getPassword());
                return getAuthenticateResponse(admin, encoder, request, admin.getPassword());
        }
        return null;
    }

    private AuthenticationResponse getAuthenticateResponse(Object obj, BCryptPasswordEncoder encoder, AuthenticateRequest request, String pass) {
//        log.info("check {} == {} is {}",request.getPassword(), pass, encoder.matches(request.getPassword(), pass));
//        log.info("check {} || {} is {}", (obj==null), (!encoder.matches(request.getPassword(), pass)), ((obj==null) || (!encoder.matches(request.getPassword(), pass))));
        if (obj == null || !encoder.matches(request.getPassword(), pass)) {
            log.info("Login Fail service -> password's not the same ");
            return new AuthenticationResponse();
        } else {
            var jwtToken = service.generateToken((UserDetails) obj);
            var jwtRefresh = service.refreshToken((UserDetails) obj);
            log.info("Login Token access: {}", jwtToken);
            log.info("Login Token refresh: {}", jwtRefresh);
            return new AuthenticationResponse(jwtToken, jwtRefresh);
        }
    }

    //-------------------------------------------------------------------------------------------
    //Saving token to cookie HttpOnly
    public void saveTokenToCookie(AuthenticationResponse authRes, HttpServletResponse response) {
        log.info("save {} to HttpOnly Cookie", authRes);
        Cookie jwtAccess = new Cookie("access", authRes.getToken());
        jwtAccess.setMaxAge(10 * 60);
        jwtAccess.setHttpOnly(true);
        jwtAccess.setPath("/");

        response.addCookie(jwtAccess);

        Cookie jwtRefresh = new Cookie("refresh", authRes.getRefresh());
        jwtRefresh.setMaxAge(20 * 60);
        jwtRefresh.setHttpOnly(true);
        jwtRefresh.setPath("/");

        response.addCookie(jwtRefresh);
    }

    //-------------------------------------------------------------------------------------------
    //Check if email is already have in DB
    public Customer isUserExist(String email) {
        return cusRep.findCustomerByCEmail(email).orElse(null);
    }

    //-------------------------------------------------------------------------------------------

    public AuthenticationResponse refreshNewToken
            (HttpServletRequest request,
             HttpServletResponse response) throws IOException {

        log.info("Refresh token to make new access token and refresh token");
//        String authHeader = request.getHeader("Authorization");
        String authHeader = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    authHeader = cookie.getValue();
                    log.info("Get token refresh from cookie {}", authHeader);
                }
            }
        }
        String userEmail;
        try {
            userEmail = service.extractUsername(authHeader);
            log.info("refresh token get: {}", userEmail);
            if (!userEmail.isEmpty()) {

                // Check if account is driver, customer, or admin
                UserDetails user = null;
                if (this.customerDetailService.loadUserByUsername(userEmail) != null) {
                    log.info("Filter customer");
                    user = this.customerDetailService.loadUserByUsername(userEmail);
                } else if (this.adminDetailService.loadUserByUsername(userEmail) != null) {
                    log.info("Filter admin");
                    user = this.adminDetailService.loadUserByUsername(userEmail);
                } else if (this.driverDetailService.loadUserByUsername(userEmail) != null) {
                    log.info("Filter driver");
                    user = this.driverDetailService.loadUserByUsername(userEmail);
                }

                log.info("refresh user: {} ", user);
                log.debug("check valid token refresh");
                if (user != null) {
                    if (service.isTokenValid(authHeader, user)) {
                        log.info("Access {} and refresh {}", service.generateToken(user), service.refreshToken(user));
                        return new AuthenticationResponse(service.generateToken(user), service.refreshToken(user));
                    }
                } else {
                    return new AuthenticationResponse();
                }
            }
        } catch (Exception e) {
            log.error("Something wrong happen");
            response.setHeader("ERROR", e.getMessage());
            response.setStatus(FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("ERROR_MSG", e.getMessage());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }

        return new AuthenticationResponse();
    }

    //-------------------------------------------------------------------------------------------
    //Update the account
    public boolean updateAccountUser(String role, String id, String username, String fname, String lname, String pass, String phone) {
        if (role.equals("CUSTOMER")) {
            log.info("update customer");
            updateCustomer(id, username, fname, lname, pass, phone);
            return true;
        } else if (role.equals("DRIVER")) {
            log.info("update driver");
            updateDriver(id, username, fname, lname, pass, phone);
            return true;
        } else if (role.equals("ADMIN")) {
            log.info("update admin");
            updateAdmin(id, username, fname, lname, pass, phone);
            return true;
        }
        return false;
    }

    private void updateCustomer(String id, String username, String fname, String lname, String pass, String phone) {
        Customer cus = Customer.builder()
                .cusId(Integer.parseInt(id))
                .cFirstname(fname)
                .cLastname(lname)
                .cPhone(phone)
                .cPassword(encoder.encode(pass))
                .CEmail(username)
                .locked(false)
                .enabled(true)
                .build();
        cusRep.save(cus);
    }

    private void updateDriver(String id, String username, String fname, String lname, String pass, String phone) {
        Driver driver = Driver.builder()
                .driverId(Integer.parseInt(id))
                .DEmail(username)
                .dFirstname(fname)
                .dLastname(lname)
                .dPassword(encoder.encode(pass))
                .dPhone(phone)
                .isActive(true)
                .dStatus(true)
                .build();
        driverRep.save(driver);
    }

    private void updateAdmin(String id, String username, String fname, String lname, String pass, String phone) {
        Admin ad = Admin.builder()
                .adId(Integer.parseInt(id))
                .adEmail(username)
                .adFirstname(fname)
                .adLastname(lname)
                .adPassword(encoder.encode(pass))
                .adPhone(phone)
                .build();
        adRep.save(ad);
    }

    //------------------------------------------------------------------------------------------------------------------
    private String buildEmail(String name, String link, String MSG) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">"+ MSG +"</p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 10 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

}

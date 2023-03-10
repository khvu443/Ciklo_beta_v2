package com.example.ciklo.auth;

import com.example.ciklo.model.Admin;
import com.example.ciklo.model.Customer;
import com.example.ciklo.model.Driver;
import com.example.ciklo.repository.AdminRepo;
import com.example.ciklo.repository.CustomerRepo;
import com.example.ciklo.repository.DriverRepo;
import com.example.ciklo.service.ConfirmToken.ConfirmTokenServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/ciklo/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController<E> {
    private final AuthenticationService service;
    private final ConfirmTokenServiceImpl confirmTokenService;
    private final CustomerRepo cusRep;
    private final DriverRepo driverRep;
    private final AdminRepo adRep;

    private final ModelAndView mav = new ModelAndView();

    //-------------Register--------------------------------------------------------------------------
    @GetMapping("/checkEmail")
    public @ResponseBody String isEmailExist(HttpServletRequest request) {
        String email = request.getParameter("email");
//        log.info("Check email from ajax: {}", email);
//        log.info("Get email check in DB -> {}", service.isUserExist(email));
        Customer u = service.isUserExist(email);
        String res = new Gson().toJson(u);
        return res;
    }

    @GetMapping("/confirm")
    public ModelAndView confirm(@RequestParam("token") String token, final RedirectAttributes redirectAttributes) {
        switch (service.confirmToken(token)) {
            case 0:
                redirectAttributes.addFlashAttribute("notice", "Confirm Success");
                redirectAttributes.addFlashAttribute("alert", "You now can go back to login");
                redirectAttributes.addFlashAttribute("link", "http://localhost:8080/ciklo/auth/authenticateForm");
                redirectAttributes.addFlashAttribute("type", "Login Page");
                mav.setViewName("redirect:/ciklo/auth/page-error");
                break;

            case 1:
                redirectAttributes.addFlashAttribute("notice", "Oops");
                redirectAttributes.addFlashAttribute("alert", "You Already Confirm, Please Login To Access Page");
                redirectAttributes.addFlashAttribute("link", "http://localhost:8080/ciklo/auth/authenticateForm");
                redirectAttributes.addFlashAttribute("type", "Login Page");
                mav.setViewName("redirect:/ciklo/auth/page-error");
                break;
            case 2:
                redirectAttributes.addFlashAttribute("notice", "Oops");
                redirectAttributes.addFlashAttribute("alert", "Your Confirm Has Expired, Please Press Re-send The Link Confirm");
                redirectAttributes.addFlashAttribute("link", "http://localhost:8080/ciklo/auth/re-confirm?token=" + token);
                redirectAttributes.addFlashAttribute("type", "Re-send Confirm");

                mav.setViewName("redirect:/ciklo/auth/page-error");
                break;
        }
        return mav;
    }

    @GetMapping("/page-error")
    public String pageError() {
        return "page-notice";
    }

    @GetMapping("/registerForm")
    public ModelAndView registerForm() {

        mav.setViewName("register");
        return mav;
    }

    @PostMapping("/register")
    public ModelAndView register(
            @ModelAttribute RegisterCusRequest request,
            HttpServletResponse response
    ) throws Exception {
        log.info("Register info : {}", request);
        AuthenticationResponse responseReg = service.register(request);

        log.info("Register Success");

        service.saveTokenToCookie(responseReg, response);
        mav.setViewName("redirect:/ciklo/auth/authenticateForm");
        return mav;
    }

    //----------------Login------------------------------------------------------------------------------

    @GetMapping("/authenticateForm")
    public String authenticateForm() {
        return "login";
    }

    @PostMapping("/authenticate")
    public ModelAndView authenticate(
            @ModelAttribute AuthenticateRequest request,
            HttpServletResponse response,
            final RedirectAttributes redirectAttributes
    ) {
        AuthenticationResponse responseLogin = service.authenticate(request);
        log.info("The token from login {}", responseLogin);


        if (responseLogin == null) {
            log.info("Login fail in controller");
            if (!cusRep.findCustomerByCEmail(request.getEmail()).get().isEnabled()) {
                redirectAttributes.addFlashAttribute("notice",
                        "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                                "                            <h5 class=\"alert-heading mt-2\"> Fail Login! Please confirm email</h5>\n" +
                                "                            <div class=\"progress\" role=\"progressbar\" aria-label=\"Example 1px high\" aria-valuenow=\"25\"\n" +
                                "                                 aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"height: 1px\" id=\"bar-pg\">\n" +
                                "                                <div class=\"progress-bar bg-info\" style=\"width: 100%\" id=\"value-bar-pg\"></div>\n" +
                                "                            </div>\n" +
                                "                        </div>");

            } else
            {
                redirectAttributes.addFlashAttribute("notice",
                        "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                                "                            <h5 class=\"alert-heading mt-2\"> Fail Login! Your email or password is wrong</h5>\n" +
                                "                            <div class=\"progress\" role=\"progressbar\" aria-label=\"Example 1px high\" aria-valuenow=\"25\"\n" +
                                "                                 aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"height: 1px\" id=\"bar-pg\">\n" +
                                "                                <div class=\"progress-bar bg-info\" style=\"width: 100%\" id=\"value-bar-pg\"></div>\n" +
                                "                            </div>\n" +
                                "                        </div>");

            }
            mav.setViewName("redirect:/ciklo/auth/authenticateForm");
        } else {
            log.info("Login Success");
            service.saveIdToCookie(request.getEmail(), response);
            service.saveTokenToCookie(responseLogin, response);

            mav.setViewName("redirect:/ciklo/homepage/");
        }
        return mav;
    }

    //--------------Refresh token auth--------------------------------------------------------------------------------
    @GetMapping("/refresh")
    public ModelAndView refreshToken(@NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response) throws IOException {

        boolean isReload = Boolean.parseBoolean(request.getParameter("reload"));

        if (isReload) {
            AuthenticationResponse responseRefresh = service.refreshNewToken(request, response);
            log.info("Refresh token {} in controller", responseRefresh);

            if (responseRefresh == null) {
                log.info("Token refresh is expired");
                mav.setViewName("redirect:/ciklo/auth/authenticateForm");
            } else {
                log.info("Refresh new token access and refresh");
                service.saveTokenToCookie(responseRefresh, response);
                mav.setViewName("redirect:/ciklo/homepage/");
            }
        }
        return mav;
    }

    @GetMapping("/re-confirm")
    public ModelAndView reConfirm(@RequestParam("token") String token) {
        Customer cus = confirmTokenService.getConfirmToken(token).get().getCus();
        service.sendLinkConfirm(cus);
        mav.setViewName("redirect:/ciklo/auth/authenticateForm");
        return mav;
    }

    //---------------Update Account-------------------------------------------------------------------------------

    @GetMapping("/updateForm")
    public ResponseEntity<?> updateAccountForm(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().iterator().next().toString().equals("CUSTOMER")) {
            return new ResponseEntity<Customer>(cusRep.findCustomerByCEmail(auth.getName()).get(), HttpStatus.OK);
        } else if (auth.getAuthorities().iterator().next().toString().equals("DRIVER")) {
            return new ResponseEntity<Driver>(driverRep.findByDEmail(auth.getName()).get(), HttpStatus.OK);
        } else if (auth.getAuthorities().iterator().next().toString().equals("ADMIN")) {
            return new ResponseEntity<Admin>(adRep.findAdminByAdEmail(auth.getName()).get(), HttpStatus.OK);
        } else return null;
    }

    @PostMapping("/updateAccount")
    public  ResponseEntity<String> updateAccount(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String id = request.getParameter("id");
        String email = request.getParameter("email");
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String phone = request.getParameter("phone");
        String pass = request.getParameter("password");
        log.info("update info: " + email + " - " + fname + " - " + lname + " - " + phone + " - " + pass);
        return new ResponseEntity<>(String.valueOf(service.updateAccountUser(
                auth.getAuthorities().iterator().next().toString() ,
                id, email,
                fname,lname,
                pass, phone)), HttpStatus.OK) ;
    }
}

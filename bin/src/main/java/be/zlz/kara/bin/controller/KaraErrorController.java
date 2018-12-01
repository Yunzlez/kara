package be.zlz.kara.bin.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class KaraErrorController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(KaraErrorController.class);

    @GetMapping("/error")
    public String errorPage(HttpServletRequest request, Model model){
        int status = Integer.parseInt(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString());
        String path = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI).toString();
        Exception ex = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        model.addAttribute("notfound", status == HttpStatus.NOT_FOUND.value());
        model.addAttribute("path", path);
        if (ex != null) {
            model.addAttribute("message", ex.getMessage());
            model.addAttribute("stacktrace", ExceptionUtils.getStackTrace(ex));
            logger.error("Exception caught", ex);
        }

        return "kara-error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}

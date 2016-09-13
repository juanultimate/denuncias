package com.denuncias.security;

import com.denuncias.domain.User;
import com.denuncias.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Spring Security success handler, specialized for Ajax requests.
 */
@Component
public class AjaxAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Inject
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication)
        throws IOException, ServletException {
        Optional<User> user =userRepository.findOneByLogin(request.getParameter("j_username"));
        if(user.get().getPhone().equals(request.getParameter("phone"))){
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
        }



    }
}

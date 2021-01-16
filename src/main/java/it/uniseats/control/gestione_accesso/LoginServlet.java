package it.uniseats.control.gestione_accesso;

import it.uniseats.model.beans.StudenteBean;
import it.uniseats.model.dao.StudenteDAO;
import it.uniseats.utils.SHA512Utils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String action = request.getParameter("action");

        if (action != null) {


            if (action.equalsIgnoreCase("Login")) {

                String email = request.getParameter("email");
                String password = request.getParameter("password");

                String encrypted = SHA512Utils.getSHA512(password);

                String redirectedPage;
                try {
                    StudenteBean bean = (StudenteBean) StudenteDAO.doQuery("doRetrieveByEmail", email);
                    if (bean != null && bean.getPassword().equals(encrypted)) {

                        request.getSession().setAttribute("logged", true);
                        request.getSession().setAttribute("user", bean);

                        //redirect to LandindPage
                        redirectedPage = "/view/LandingPageView.jsp";
                        response.sendRedirect(request.getContextPath() + redirectedPage);
                    } else {
                        String message = "Username e/o password non validi!";
                        request.setAttribute("errore", message);
                        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/view/login/LoginView.jsp");
                        dispatcher.forward(request, response);
                    }


                } catch (Exception e) {
                    System.out.println(e);
                }

            }

        } else {
            response.sendRedirect(request.getContextPath() + "/view/login/LoginView.jsp");
        }
    }


}


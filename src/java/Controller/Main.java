/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import Beans.User;
import Model.Users;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Toshiba
 */
@WebServlet(name = "Main", urlPatterns = {"/Main"})
public class Main extends HttpServlet {
    
    HttpSession session;
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        session = request.getSession();
        String action = request.getParameter("action");
        String page = null;
        User user = null;
        request.setAttribute("user", user);
        if (action == null || session.equals(null)) {
            request.getRequestDispatcher("Index.jsp").forward(request, response);
            return;
        }else{
            if(action.equals("logout")){
                session.invalidate();
                response.sendRedirect("Index.jsp");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        session = request.getSession();
        String action = request.getParameter("action");
        PrintWriter out = response.getWriter();
        User user = new User();
        if (action != null) {
            if (action.equals("login")) {
                System.out.println("login");
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                System.out.println(username+"  uname");
                System.out.println(password+"  pass");
                Users users = new Users();
                user = users.checkLogin(username, password);
                if (!"1".equals(user.getFound())) {
                    System.out.println("Invalid username or password");
                    request.setAttribute("error", "Invalid username or password");
                    request.getRequestDispatcher("Index.jsp").forward(request, response);
                    response.sendRedirect("Index.jsp");
                    return;
                }

                session.setAttribute("user", user);
                request.setAttribute("user", user);               
                if (user.getTypeId().equals("4") || user.getTypeId().equals("5") ) {
                    request.getRequestDispatcher("Index.jsp").forward(request, response);
                    response.sendRedirect("Index.jsp");
                    return;
                }else{
                    user.setError("user type not found");
                    System.out.println("user type not found");
                    request.setAttribute("user", user);
                    request.getRequestDispatcher("Index.jsp").forward(request, response);
                    response.sendRedirect("Index.jsp");
                    return;
                }               
            }else if(action.equals("checkusername")){
                Users users = new Users();
                String username = request.getParameter("username");
                int available = users.checkUsername(username);
                String y="";
                if (available == 1){
                    y = "<font style='font-size:80%;' color = '#008080'>Available</font>";
                }else{
                    y = "<font style='font-size:80%;' color = '#FF2F00'>Already Exist</font>";
                }
                response.getWriter().write(y);
            }else if(action.equals("signup")){
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String email = request.getParameter("email");
                System.out.println(username);
                System.out.println(password);
                System.out.println(email);
                Users users = new Users();
                User user1 = new User();
                user1.setUsername(username);
                user1.setPassword(password);
                user1.setEmail(email);
                user1 = users.signup(user1);
                response.getWriter().write(user1.getSuccess());
            }
        }else{
            request.getRequestDispatcher("Index.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

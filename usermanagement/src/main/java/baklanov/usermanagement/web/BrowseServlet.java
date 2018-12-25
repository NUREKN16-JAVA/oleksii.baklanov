package baklanov.usermanagement.web;

import baklanov.usermanagement.User;
import baklanov.usermanagement.db.DaoFactory;
import baklanov.usermanagement.db.DatabaseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;


public class BrowseServlet extends HttpServlet {

    protected void service(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        if(req.getParameter("addButton")!=null){
            add(req,resp);
        }
        else if (req.getParameter("editButton")!=null){

            edit(req,resp);
        }
        else if(req.getParameter("deteleButton")!=null){
            delete(req,resp);
        }
        else if (req.getParameter("detailsButton")!=null){
            details(req,resp);
        }
        else {
            browse(req, resp);
        }
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
        String idStr = req.getParameter("id");
        selectedUser(req,resp,idStr);
        try {
            User user = DaoFactory.getInstance().getUserDao().find(new Long(idStr));
            DaoFactory.getInstance().getUserDao().delete(user);
            req.getSession().setAttribute("user", user);
        } catch (DatabaseException e) {
            req.setAttribute("error", "ERROR:" + e.toString());

        }
        browse(req, resp);
    }

    private void selectedUser(HttpServletRequest req, HttpServletResponse resp, String idStr) throws ServletException, IOException {

        if (idStr == null || idStr.trim().length() == 0) {
            req.setAttribute("error", "You must select a user");
            req.getRequestDispatcher("/browse.jsp").forward(req, resp);
            return;
        }
    }

    private void setAttributesInRequestById(HttpServletRequest req, HttpServletResponse resp, String idStr) throws ServletException, IOException {
        try {
            User user = DaoFactory.getInstance().getUserDao().find(new Long(idStr));
            req.getSession().setAttribute("user", user);
        } catch (Exception e) {
            req.setAttribute("error", "ERROR:" + e.toString());
            req.getRequestDispatcher("/browse.jsp").forward(req, resp);
            return;
        }
    }

    private void details(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
        String idStr = req.getParameter("id");
        selectedUser(req,resp,idStr);
        setAttributesInRequestById(req,resp,idStr);
        req.getRequestDispatcher("/details").forward(req, resp);
    }

    private void edit(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
        String idStr = req.getParameter("id");
        selectedUser(req,resp,idStr);
        setAttributesInRequestById(req,resp,idStr);
        req.getRequestDispatcher("/edit").forward(req,resp);
    }

    private void add(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
        req.getRequestDispatcher("/add").forward(req,resp);
    }

    private void browse(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Collection users;
        try {
            users=DaoFactory.getInstance().getUserDao().findAll();
            req.getSession().setAttribute("users", users);
            req.getRequestDispatcher("/browse.jsp").forward(req,resp);
        } catch (DatabaseException e) {
            throw new ServletException(e);
        }
    }

}

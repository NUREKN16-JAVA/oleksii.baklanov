package baklanov.usermanagement.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DetailsServlet extends BrowseServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("okButton") != null) {
            req.getRequestDispatcher("/browse").forward(req, resp);
        }
        else {
            req.getRequestDispatcher("/details.jsp").forward(req, resp);
        }
    }
}

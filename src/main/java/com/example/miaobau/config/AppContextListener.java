package com.example.miaobau.config;

import com.example.miaobau.dao.CategoryDAO;
import com.example.miaobau.dao.SpeciesDAO;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
       CategoryDAO categoryDAO = new CategoryDAO();
       SpeciesDAO speciesDAO = new SpeciesDAO();
        try {
            sce.getServletContext().setAttribute("categories", categoryDAO.doRetriveAll());
            sce.getServletContext().setAttribute("species", speciesDAO.doRetriveAll());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

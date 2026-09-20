package com.example.miaobau.config;

import com.example.miaobau.dao.CategoryDAO;
import com.example.miaobau.dao.SpeciesDAO;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

/*
 * Listener eseguito UNA sola volta all'avvio dell'applicazione (contextInitialized).
 * Precarica categorie e specie nel ServletContext (ambito globale, condiviso da
 * tutte le pagine e tutti gli utenti).
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        CategoryDAO categoryDAO = new CategoryDAO();
        SpeciesDAO speciesDAO = new SpeciesDAO();
        try {
            // Recupera le liste dal DB e le mette nel ServletContext, disponibili
            // a tutte le JSP/servlet.
            sce.getServletContext().setAttribute("categories", categoryDAO.doRetriveAll());
            sce.getServletContext().setAttribute("species", speciesDAO.doRetriveAll());
        } catch (SQLException e) {
            // Se il precaricamento fallisce, l'app è di fatto inutilizzabile:
            // faccio fallire all'avvio
            throw new RuntimeException(e);
        }
    }
}
package br.com.lnt.filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.hibernate.HibernateException;

import br.com.lnt.managedbean.Config;

@WebFilter(servletNames = { "Faces Servlet" })
public class JPAFilter implements Filter {

	private static EntityManagerFactory factory;
	private Config conf = new Config(new ConexaoGeral().getPersistence());

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Map pmap = new HashMap();
		pmap.put(this.conf.Conf().getProperty("jdbc.user"), conf.Conf().getProperty("jdbc.user.value"));
		pmap.put(this.conf.Conf().getProperty("jdbc.password"), conf.Conf().getProperty("jdbc.password.value"));
		pmap.put(this.conf.Conf().getProperty("jdbc.url"), conf.Conf().getProperty("jdbc.url.value"));
		pmap.put(this.conf.Conf().getProperty("hibernate.connection.isolation"), conf.Conf().getProperty("hibernate.connection.isolation.value"));
		factory = Persistence.createEntityManagerFactory("lntintelligence", pmap);
	}

	@Override
	public void destroy() {
		factory.close();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// CHEGADA
		EntityManager manager = factory.createEntityManager();
		request.setAttribute("EntityManager", manager);
		try {
			manager.getTransaction().begin();
			// CHEGADA

			// FACES SERVLET
			chain.doFilter(request, response);
			// FACES SERVLET

			// SAIDA
			manager.getTransaction().commit();
		} catch (HibernateException e) {
			manager.getTransaction().rollback();
			e.getMessage();
		} finally {
			manager.close();
		}
		// SAIDA
	}

	public static EntityManager getManager() {
		return factory.createEntityManager();
	}
}
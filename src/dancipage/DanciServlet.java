package dancipage;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DanciServlet extends HttpServlet {
	
	private static final Map<String, String> passwords = new HashMap<>();
	static {
		passwords.put("Danci", "icnaD");
		passwords.put("KoicsD", "blabla");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		Object username = session.getAttribute("username");
		if (username != null && passwords.containsKey(username))
			greetUser(out, (String)username);
		else
			redirectUserToLogin(out);
		out.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		out.println("Thank you for posting me your data!"); //TODO: post-password logic
		//session.invalidate();
		//session.setAttribute("username", "Danci");
		out.close();
	}

	private void greetUser(PrintWriter out, String username) {
		String title = "Profile";
		String body = "<h1>Welcome, " + username + ". Have a nice day.</h1>";
		out.print(generateHTML(title, body));
	}
	
	private void redirectUserToLogin(PrintWriter out) {
		String title = "UnauthorizedAccess";
		String body = "";
		out.print(generateHTML(title, body));
	}
	
	private void warnUserAlreadyLoggedIn(PrintWriter out) {
		String title = "AlreadyLoggedIn";
		String body = "";
		out.print(generateHTML(title, body));
	}
	
	private String generateHTML(String title, String body) {
		String html = "";
		html += "<html>\n";
		html += "<head>\n";
		html += "<title>" + title + "</title>\n";
		html += "</head>\n";
		html += "<body>\n";
		html += body;
		html += "</body>\n";
		html += "</html>\n";
		return html;
	}
	
}

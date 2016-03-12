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
		PrintWriter outp = resp.getWriter();
		
		Object username = session.getAttribute("username");
		if (username != null && passwords.containsKey(username))
			executeCommand(session, outp);
		else
			redirectUserToLogin(outp);
		outp.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter outp = resp.getWriter();
		
		String usernameFromForm = req.getParameter("username");
		String passwordFromForm = req.getParameter("password");
		Object usernameInSession = session.getAttribute("username");
		if (passwords.containsKey(usernameFromForm) && passwords.get(usernameFromForm).equals(passwordFromForm)) {
			if (usernameInSession != null && usernameInSession.equals(usernameFromForm)) {
				warnUserAlreadyLoggedIn(outp);
			}
			else {
				session.setAttribute("username", usernameFromForm);
				executeCommand(session, outp);
			}
		} else {
			denyAccess(outp);
		}
		outp.close();
	}
	
	private void executeCommand(HttpSession session, PrintWriter outp) {
		switch (getInitParameter("command")) {
		case "view":
			greetUser(outp, (String)session.getAttribute("username"));
			break;
		case "logout":
			session.invalidate();
			sayUserGoodbye(outp);
		}
	}

	private void greetUser(PrintWriter outp, String username) {
		String title = "Profile";
		String body = "<h1>Welcome, " + username + ". Have a nice day.</h1>\n"
				+ "<p><form action=\"logout.html\" method=\"GET\">"
				+ "<input type=\"submit\" value=\"Logout\">"
				+ "</form></p>\n"
				+ "<p><a href=\"index.html\">main page</a></p>\n";
		outp.print(generateHTML(title, body));
	}
	
	private void sayUserGoodbye(PrintWriter outp) {
		String title = "Logout";
		String body = "<h1>Good Bye.</h1>\n"
				+ "<p>You have been logged out successfuly.</p>\n"
				+ "<p><a href=\"index.html\">main page</a></p>\n"
				+ "<p><a href=\"login.html\">login</a></p>\n";
		outp.print(generateHTML(title, body));
	}
	
	private void denyAccess(PrintWriter outp) {
		String title = "AccessDenied";
		String body = "<h1>Access denied!</h1>\n"
				+ "<p>Invalid user name or password.</p>\n"
				+ "<p><a href=\"login.html\">retry</a></p>\n"
				+ "<p><a href=\"index.html\">main page</a></p>\n";
		outp.print(generateHTML(title, body));
	}
	
	private void redirectUserToLogin(PrintWriter outp) {
		String title = "WhoAreYou";
		String body = "<h1>Who are you?</h1>\n"
				+ "<p>You have to be logged in to access this page.</p>\n"
				+ "<p><a href=\"login.html\">login</a></p>\n"
				+ "<p><a href=\"index.html\">main page</a></p>\n";
		outp.print(generateHTML(title, body));
	}
	
	private void warnUserAlreadyLoggedIn(PrintWriter outp) {
		String title = "AlreadyLoggedIn";
		String body = "<h1>You are already logged in.</h1>\n"
				+ "<p>Please, choose what you would like to do.</p>\n"
				+ "<p><form action=\"profile.html\" method=\"GET\">"
				+ "<input type=\"submit\" value=\"View Profile\">"
				+ "</form></p>\n"
				+ "<p><form action=\"logout.html\" method=\"GET\">"
				+ "<input type=\"submit\" value=\"Logout\">"
				+ "</form></p>\n";
		outp.print(generateHTML(title, body));
	}
	
	private String generateHTML(String title, String body) {
		return "<html>\n"
				+ "<head>\n"
				+ "<title>" + title + "</title>\n"
				+ "</head>\n"
				+ "<body>\n"
				+ body
				+ "</body>\n"
				+ "</html>\n";
	}
	
}

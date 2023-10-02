package Controller;

import ControllerHelper.AgentHelper;
import ControllerHelper.BranchHelper;
import ControllerHelper.CommonConstant;
import ControllerHelper.FuntionHelper;
import ControllerHelper.GetUserCredentialsFactory;
import ControllerHelper.InterfaceAgent;
import ControllerHelper.InterfaceBranch;
import ControllerHelper.validationHelper;
import Model.Agent;
import Model.Branch;
import Model.UserCredentials;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.json.JSONObject;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)
public class AgentServlet extends HttpServlet {
    
    /*
     *creating object from AgentHelper with reference to InterfaceAgent interface
     */
    InterfaceAgent interfaceAgent = new AgentHelper();
    
     /*
     *creating object from AgentHelper with reference to InterfaceAgent interface
     */
    InterfaceBranch interfaceBranch = new BranchHelper();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       

        String action = request.getServletPath();
        switch (action) {
            case "/agentSignin":
                agentSignin(request, response);
                break;
            case "/agentSignOut":
                agentSignOut(request, response);
                break;
            case "/agentInsert":
                insertAgent(request, response);
                break;
            case "/agentDelete":
                deleteAgent(request, response);
                break;
            case "/agentEdit":
                showEditAgentForm(request, response);
                break;
            case "/agentUpdate":
                updateAgent(request, response);
                break;
            case "/agentProfilePage":
                agentProfilePage(request, response);
                break;
            case "/agentChangePassword":
                agentChangePassword(request, response);
                break;
            case "/agentEditProfile":
                agentEditProfile(request, response);
                break;
            case "/agentGoogleRedirect":
                agentGoogleRedirect(request, response);
                break;
            case "/agentGenerateToken":
                agentGenerateToken(request, response);
                break;
            default:
                listAgent(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void listAgent(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        List<Agent> agentList = interfaceAgent.getAllAgent();
        request.setAttribute("agentList", agentList);
        List<Branch> branchList = interfaceBranch.getAllBranch();
        request.setAttribute("branchList", branchList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("Views/Admin/manage_agent.jsp");
        dispatcher.forward(request, response);
    }

    private void insertAgent(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        Agent agent = new Agent(
                request.getParameter("name"),
                request.getParameter("nic"),
                request.getParameter("email"),
                FuntionHelper.randomPassword(),
                request.getParameter("branch_id")
        );

        if (interfaceAgent.insertAgent(agent) > 0) {
            listAgent(request, response);
        } else {
            request.getRequestDispatcher("Views/Admin/fail.jsp").forward(request, response);
        }
    }

    private void deleteAgent(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        if (interfaceAgent.deleteAgent(id) > 0) {
            listAgent(request, response);
        } else {
            request.getRequestDispatcher("Views/Admin/fail.jsp").forward(request, response);
        }

    }

    private void showEditAgentForm(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        Agent editAgent = interfaceAgent.selectAgent(id);
        request.setAttribute("editAgent", editAgent);
        listAgent(request, response);
    }

    private void updateAgent(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        Agent agent = new Agent(
                Integer.parseInt(request.getParameter("id")),
                request.getParameter("name"),
                request.getParameter("nic"),
                request.getParameter("email"),
                request.getParameter("password"),
                request.getParameter("branch_id")
        );

        if (interfaceAgent.updateAgent(agent) > 0) {
            listAgent(request, response);
        } else {
            request.getRequestDispatcher("Views/Admin/fail.jsp").forward(request, response);
        }
    }

    private void agentSignin(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        if (new validationHelper().isValidEmailAddress(request.getParameter("email")) == false) {
            JSONObject jsonData = new JSONObject();
            jsonData.put("status", "invalidEmail");
            response.setContentType("application/json");
            response.getWriter().print(jsonData.toString());
        } else {
            // create GetUserCredentialsFactory object
             GetUserCredentialsFactory getUserCredentialsFactory = new GetUserCredentialsFactory();
              // get agent type object
             UserCredentials agent = getUserCredentialsFactory.getUserCredentials("agent", request.getParameter("email"), request.getParameter("password"));

            if (interfaceAgent.validateAgent(agent) > 0) {
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(1800);
                String id = String.valueOf(interfaceAgent.validateAgent(agent));
                session.setAttribute("USER_ID", id);
                session.setAttribute("USER_TYPE", "agent");
                // request.getRequestDispatcher("Views/Admin/index.jsp").forward(request, response);
                JSONObject jsonData = new JSONObject();
                jsonData.put("status", "pass");
                response.setContentType("application/json");
                response.getWriter().print(jsonData.toString());
            } else {
                // request.getRequestDispatcher("Views/Admin/fail.jsp").forward(request, response);
                JSONObject jsonData = new JSONObject();
                jsonData.put("status", "fail");
                response.setContentType("application/json");
                response.getWriter().print(jsonData.toString());
            }
        }
    }

    private void agentProfilePage(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.getRequestDispatcher("Views/Admin/profile.jsp").forward(request, response);
    }

    private void agentSignOut(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        session.removeAttribute("USER_ID");
        request.getRequestDispatcher("Views/Admin/signin.jsp").forward(request, response);
    }

    private void agentChangePassword(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        final String curuntPassword = request.getParameter("current_password");
        final String newPassword = request.getParameter("new_password");

        if (interfaceAgent.selectChangePasswordAgent(id, curuntPassword) > 0) {
            if (interfaceAgent.changePasswordAgent(id, newPassword) > 0) {
//                    adminProfilePage(request, response);
                JSONObject jsonData = new JSONObject();
                jsonData.put("status", "pass");
                response.setContentType("application/json");
                response.getWriter().print(jsonData.toString());
            } else {
                JSONObject jsonData = new JSONObject();
                jsonData.put("status", "fail");
                response.setContentType("application/json");
                response.getWriter().print(jsonData.toString());
            }
        } else {
            JSONObject jsonData = new JSONObject();
            jsonData.put("status", "invalidCurrent");
            response.setContentType("application/json");
            response.getWriter().print(jsonData.toString());
        }

    }

    private void agentEditProfile(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String fileName = null;

            Part part = request.getPart("image");

            fileName = FuntionHelper.extractFileName(part);
            part.write(CommonConstant.AGENT_IMAGE_UPLOAD_DIR + fileName);

            Agent agent = new Agent(
                    Integer.parseInt(request.getParameter("id")),
                    request.getParameter("name"),
                    request.getParameter("nic"),
                    request.getParameter("email"),
                    fileName
            );

            if (interfaceAgent.updateProfileImageAndData(agent) > 0) {
                agentProfilePage(request, response);
            } else {
                request.getRequestDispatcher("Views/Admin/fail.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            Agent agent = new Agent(
                    Integer.parseInt(request.getParameter("id")),
                    request.getParameter("name"),
                    request.getParameter("nic"),
                    request.getParameter("email")
            );

            if (interfaceAgent.updateProfileData(agent) > 0) {
                agentProfilePage(request, response);
            } else {
                request.getRequestDispatcher("Views/Admin/fail.jsp").forward(request, response);
            }
        }
    }
    
    private void agentGoogleRedirect(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String clientId = "880290326863-0j7hjqlijpt4947knag7legvh2856cof.apps.googleusercontent.com";
        String redirectURI = "http://localhost:8080/Ryptor---Java-Servlet-main/agentGenerateToken";
        String authURL = "https://accounts.google.com/o/oauth2/auth" +
                         "?client_id=" + clientId +
                         "&redirect_uri=" + redirectURI +
                         "&response_type=code" +
                         "&scope=openid email profile"; // Customize scopes as needed

        response.sendRedirect(authURL);
    }
    
    private void agentGenerateToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException { 
        String code = request.getParameter("code");
        String clientId = "880290326863-0j7hjqlijpt4947knag7legvh2856cof.apps.googleusercontent.com";
        String clientSecret = "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";
        String redirectURI = "http://localhost:8080/Ryptor---Java-Servlet-main/agentGenerateToken";
        String tokenURL = "https://accounts.google.com/o/oauth2/token";
        String userInfoURL = "https://www.googleapis.com/oauth2/v1/userinfo";
        
        String accessToken = exchangeCodeForAccessToken(code, clientId, clientSecret, redirectURI, tokenURL);
        
        if (accessToken != null) {
            // Fetch user information using the access token
            String userInfo = fetchUserInfo(accessToken, userInfoURL);
            String email = extractEmail(userInfo);
            Agent agent = interfaceAgent.selectAgent(email);
            
            if (agent != null) {
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(1800);
                session.setAttribute("USER_ID", String.valueOf(agent.getId()));
                session.setAttribute("USER_TYPE", "agent");
                request.getRequestDispatcher("Views/Admin/index.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("Views/Admin/signin.jsp").forward(request, response);
            }
        } else {
            // Handle the case where the access token couldn't be obtained
            request.getRequestDispatcher("Views/Admin/signin.jsp").forward(request, response);
        }
    }
    
    private String exchangeCodeForAccessToken(String code, String clientId, String clientSecret, 
            String redirectURI, String tokenURL) throws IOException {
           
        // Construct the POST request to exchange the code for an access token
        URL url = new URL(tokenURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        String postData = "code=" + code +
                          "&client_id=" + clientId +
                          "&client_secret=" + clientSecret +
                          "&redirect_uri=" + redirectURI +
                          "&grant_type=authorization_code";

        connection.getOutputStream().write(postData.getBytes());

        // Read the response
        InputStream responseStream = connection.getInputStream();
        Scanner scanner = new Scanner(responseStream).useDelimiter("\\A");
        String response = scanner.hasNext() ? scanner.next() : "";

        String accessToken = extractAccessToken(response);

        return accessToken;
    }
    
    private static String extractAccessToken(String jsonString) {
        int startIndex = jsonString.indexOf("\"access_token\": \"") + "\"access_token\": \"".length();
        int endIndex = jsonString.indexOf("\"", startIndex);

        if (startIndex != -1 && endIndex != -1) {
            return jsonString.substring(startIndex, endIndex);
        } else {
            return null;
        }
    }
    
    private static String extractEmail(String jsonString) {
        int startIndex = jsonString.indexOf("\"email\": \"") + "\"email\": \"".length();
        int endIndex = jsonString.indexOf("\"", startIndex);

        if (startIndex != -1 && endIndex != -1) {
            return jsonString.substring(startIndex, endIndex);
        } else {
            return null;
        }
    }
    
    private String fetchUserInfo(String accessToken, String userInfoURL) throws IOException {
        // Make a GET request to fetch user information
        URL url = new URL(userInfoURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        // Read the response
        InputStream responseStream = connection.getInputStream();
        Scanner scanner = new Scanner(responseStream).useDelimiter("\\A");
        String userInfo = scanner.hasNext() ? scanner.next() : "";

        return userInfo;
    }

}

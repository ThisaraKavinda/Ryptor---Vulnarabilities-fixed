<%@page import="ControllerHelper.AgentHelper"%>
<%@page import="Model.Agent"%>
<%@page import="Model.Branch"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.List"%>
<%String AGENT_DRI = "img/agent/"; %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
   <head>
       <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Agent</title>
        <link href="css/modern.css" rel="stylesheet" type="text/css"/>
    </head>
      <body>



        <div class="wrapper">

            <jsp:include page="/include/admin_side_nav.jsp"/>

            <div class="main">

                <jsp:include page="/include/admin_top_nav.jsp"/>

                <main class="content">
                    <div class="container-fluid">

                        <div class="header">
                            <h1 class="header-title">
                                <%if (request.getAttribute("editAgent") != null) {%>
                                Edit Agent
                                <%} else {%>
                                Add New Agent
                                <%}%>
                            </h1>

                        </div>

                        <!--From start-->
                        <div class="col-md-12">
                            <div class="card">
                                <div class="card-body" style="margin-top: 20px;">
                                    <%if (request.getAttribute("editAgent") == null) {%>
                                    <form action="agentInsert" method="post">
                                        <%}%>
                                        <%if (request.getAttribute("editAgent") != null) {%>
                                        <form action="agentUpdate" method="post">
                                            <%}%>
                                            <div class="row">
                                                <div class="mb-3 col-md-6">
                                                    <label for="inputEmail4">Name</label>
                                                    <input type="text" class="form-control"   value="${fn:escapeXml(editAgent.name)}" name="name" >
                                                </div>
                                                <div class="mb-3 col-md-6">
                                                    <label for="inputPassword4">Email</label>
                                                    <input type="email" class="form-control" id="inputPassword4"  value="${fn:escapeXml(editAgent.email)}"  name="email">
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="mb-3 col-md-6">
                                                    <label for="inputAddress">NIC</label>
                                                    <input type="text" class="form-control" id="inputAddress"  value="${fn:escapeXml(editAgent.nic)}" name="nic">

                                                </div>
                                                <div class="mb-3 col-md-6">
                                                    <label for="inputState">Branch</label>
                                                    <select id="inputState" class="form-control" name="branch_id" >
                                                        <%List<Branch> branchList = (ArrayList<Branch>) request.getAttribute("branchList");%>
                                                        <% for (Branch branch : branchList) {%>
                                                        <%if (AgentHelper.BRANCHNAME != null) {%>
                                                        <%if (AgentHelper.BRANCHNAME.equals(branch.getName())) {%>
                                                        <option selected="" value="<%=branch.getId()%>"><%=branch.getName()%></option> 
                                                        <%} else {%>
                                                        <option  value="<%=branch.getId()%>"><%=branch.getName()%></option> 
                                                        <%}%>
                                                        <%} else {%>
                                                        <option  value="<%=branch.getId()%>"><%=branch.getName()%></option> 
                                                        <%}%>
                                                        <%}%>
                                                    </select>
                                                </div>
                                            </div>


                                            <%if (request.getAttribute("editAgent") != null) {%>
                                            <div class="row">
                                                <div class="mb-3 col-md-6">
                                                    <label for="inputEmail4">Password</label>
                                                    <input type="hidden"  name="id" value="${fn:escapeXml(editAgent.id)}" >
                                                    <input type="test" class="form-control" name="password" value="${fn:escapeXml(editAgent.password)}">
                                                </div>
                                            </div>
                                            <%}%>

                                            <button type="submit" class="btn btn-primary">Submit</button>



                                        </form>
                                </div>
                            </div>
                        </div>

                        <!--Table start-->
                        <div class="col-12">
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="card-title">Admin Table</h5>
                                </div>
                                <div class="card-body">
                                    <table id="datatables-basic" class="table table-striped" style="width:100%">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Name</th>
                                                <th>NIC</th>
                                                <th>Email</th>
                                                <th>Password</th>
                                                <th>Branch</th>
                                                <th>Action</th>
                                            </tr>
                                        </thead>

                                        <tbody>

                                   
                                            <%List<Agent> agentList = (ArrayList<Agent>) request.getAttribute("agentList");
                                                for (Agent agent : agentList) {%>

                                            <tr>
                                                <td><c:out value="${agent.getId()}"/></td>
                                                  <%if(agent.getImage() == null){%>
                                                    <td><img src="img/defult_avatar.jpg" class="rounded-circle me-2" alt="Avatar" width="48" height="48"><c:out value="${agent.getName()}"/></td>
                                                 <%} else {%>
                                                    <td><img src="<c:out value="${AGENT_DRI+agent.getImage()}"/>" class="rounded-circle me-2" alt="Avatar" width="48" height="48"><c:out value="${agent.getName()}"/></td>
                                                 <%}%>
                                                <td><c:out value="${agent.getNic()}"/></td>
                                                <td><c:out value="${agent.getEmail()}"/></td>
                                                <td><c:out value="${agent.getPassword()}"/></td>
                                                <td><c:out value="${agent.getBranchId()}"/></td>
                                                <td class="table-action">
                                                    <a href="agentEdit?id=<c:out value="${agent.getId()}"/>" style="margin-left: 8px"><i class="align-middle fas fa-fw fa-pen"></i></i></a>
                                                    <a href="agentDelete?id=<c:out value="${agent.getId()}"/> style="margin-left: 8px"><i class="align-middle fas fa-fw fa-trash"></i></a>
                                                </td>
                                            </tr>
                                            <%}%>
                                        </tbody>

                                    </table>
                                </div>
                            </div>
                        </div>



                    </div>
                </main>




            </div>



        </div>





        <script src="js/app.js"></script>
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                // Datatables basic
                $('#datatables-basic').DataTable({
                    responsive: true
                });
                // Datatables with Buttons
                var datatablesButtons = $('#datatables-buttons').DataTable({
                    lengthChange: !1,
                    buttons: ["copy", "print"],
                    responsive: true
                });
                datatablesButtons.buttons().container().appendTo("#datatables-buttons_wrapper .col-md-6:eq(0)")
            });
        </script>
    </body>
</html>

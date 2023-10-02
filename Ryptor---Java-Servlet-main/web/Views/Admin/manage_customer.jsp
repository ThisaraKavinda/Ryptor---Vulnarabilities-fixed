<%@page import="ControllerHelper.CSRF"%>
<%@page import="Model.Customer"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
String csrfToken = CSRF.getToken();
javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie("csrf", csrfToken);
response.addCookie(cookie);
%>


<%String id = (String) session.getAttribute("USER_ID");%>
<%String userType = (String) session.getAttribute("USER_TYPE");%>

<html>
    <head>    
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Customer Manage</title>
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
                                <%if (request.getAttribute("editCustomer") != null) {%>
                                Edit Customer
                                <%} else {%>
                                Add New Customer
                                <%}%>
                            </h1>

                        </div>

                        <!--From start-->
                        <div class="col-md-12">
                            <div class="card">
                                <div class="card-body" style="margin-top: 20px;">
                                    <%if (request.getAttribute("editCustomer") == null) {%>
                                    <form action="customerInsert" method="post">
                                        <%}%>
                                        <%if (request.getAttribute("editCustomer") != null) {%>
                                        <form action="customerUpdate" method="post">
                                            <%}%>
                                            <input type="hidden" name="csrfToken" value="<%= csrfToken %>"/>
                                            <div class="row">
                                                <div class="mb-3 col-md-6">
                                                    <label for="inputEmail4">Name</label>
                                                    <input type="text" class="form-control"   value="${fn:escapeXml(editCustomer.name)}" name="name" required >
                                                </div>
                                                <div class="mb-3 col-md-6">
                                                    <label for="inputPassword4">Email</label>
                                                    <input type="email" class="form-control" id="inputPassword4"  value="${fn:escapeXml(editCustomer.email)}"  name="email" required>
                                                </div>
                                            </div>

                                            <%if (request.getAttribute("editCustomer") != null) {%>
                                            <div class="row">
                                                <div class="mb-3 col-md-6">
                                                    <label for="inputEmail4">User Name</label>
                                                    <input type="hidden"  name="id" value="${fn:escapeXml(editCustomer.id)}" >
                                                    <input type="text" class="form-control"  name="user_name" value="${fn:escapeXml(editCustomer.userName)}" required >
                                                </div>
                                                <div class="mb-3 col-md-6">
                                                    <label for="inputPassword4">Password</label>
                                                    <input type="test" class="form-control" name="password" value="${fn:escapeXml(editCustomer.password)}" required >
                                                </div>
                                            </div>
                                            <%}%>

                                            <div class="mb-3 ">
                                                <label for="inputAddress">Address</label>
                                                <input type="text" class="form-control" id="inputAddress"  value="${fn:escapeXml(editCustomer.address)}" name="address" required >

                                            </div>

                                            <div class="row">
                                                <div class="mb-3 col-md-6">
                                                    <label for="inputCity">NIC</label>
                                                    <input type="text" class="form-control"  value="${fn:escapeXml(editCustomer.nic)}" name="nic" required >
                                                </div>
                                                <div class="mb-3 col-md-6">
                                                    <label for="inputCity">Contact No</label>
                                                    <input type="text" class="form-control"  value="${fn:escapeXml(editCustomer.contactNo)}" name="contactNo" required >
                                                </div>                                       

                                            </div>


                                            <button type="submit" class="btn btn-primary">Submit</button>



                                        </form>
                                </div>
                            </div>
                        </div>

                        <!--Table start-->
                        <div class="col-12">
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="card-title">Customer Table</h5>
                                </div>
                                <div class="card-body">
                                    <table id="datatables-basic" class="table table-striped" style="width:100%">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Name</th>
                                                <th>Email</th>
                                                <th>NIC</th>
                                                <th>Address</th>
                                                <th>Contact No</th>
                                                <th>User Name</th>
                                                <th>Password</th>
                                                <th>Action</th>
                                            </tr>
                                        </thead>

                                        <tbody>

                                            <c:forEach var="customer" items="${customerList}">
                                                <tr>
                                                    <td><c:out value="${customer.getId()}" /></td>
                                                    <td><c:out value="${customer.getName()}" /></td>
                                                    <td><c:out value="${customer.getEmail()}" /></td>
                                                    <td><c:out value="${customer.getNic()}" /></td>
                                                    <td><c:out value="${customer.getAddress()}" /></td>
                                                    <td><c:out value="${customer.getContactNo()}" /></td>
                                                    <td><c:out value="${customer.getUserName()}" /></td>
                                                    <td><c:out value="${customer.getPassword()}" /></td>
                                                    <td class="table-action">
                                                    <a href="customerEdit?id=<c:out value="${customer.getId()}" />" style="margin-left: 8px"><i class="align-middle fas fa-fw fa-pen"></i></i></a>
                                                  <%if ("admin".equals(userType) || "manager".equals(userType)) {%>
                                                    <a href="customerDelete?id=<c:out value="${customer.getId()}" />" style="margin-left: 8px"><i class="align-middle fas fa-fw fa-trash"></i></a>
                                                  <%}%>
                                                </td>
                                                </tr>
                                            </c:forEach>
                                                
                                            
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
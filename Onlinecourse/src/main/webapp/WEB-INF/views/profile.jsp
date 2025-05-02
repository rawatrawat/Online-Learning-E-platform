<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Profile</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
<c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
</c:if>
<c:if test="${not empty success}">
    <div class="alert alert-success">${success}</div>
</c:if>
<div class="container mt-5">
    <form action="/profile/update" method="post" class="mt-4">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <h3>Profile (press to update your profile)</h3>
        <input type="hidden" name="username" value="${user.username}">
        <div class="form-group">
            <label>Username</label>
            <input type="text" class="form-control" value="${user.username}" readonly>
        </div>
        <div class="form-group">
            <label>Full Name</label>
            <input type="text" class="form-control" name="fullName"
                   value="${user.fullName}" required>
        </div>
        <div class="form-group">
            <label>Email</label>
            <input type="email" class="form-control" name="email"
                   value="${user.email}" required>
        </div>

        <div class="form-group">
            <label>Phone Number</label>
            <input type="text" class="form-control" name="phoneNumber"
                   value="${user.phoneNumber}" required>
        </div>

        <div class="form-group">
            <label>New Password (leave blank to keep current)</label>
            <input type="password" class="form-control" name="newPassword" minlength="6" maxlength="20">
        </div>

        <div class="form-group">
            <label>Confirm New Password</label>
            <input type="password" class="form-control" name="confirmPassword" minlength="6" maxlength="20">
        </div>

        <button type="submit" class="btn btn-primary">Update Profile</button>
    </form>

    <!-- Only for teacher -->
    <div class="mt-4">
        <a href="/" class="btn btn-secondary">Back to Home</a>
        <c:if test="${user.role eq 'TEACHER'}">
            <a href="/admin/users" class="btn btn-info ml-2">Manage Users</a>
        </c:if>
    </div>
</div>
</body>
</html>
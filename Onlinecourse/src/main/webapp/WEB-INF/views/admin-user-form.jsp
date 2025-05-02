<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>${user.id == null ? 'Add User' : 'Edit User'}</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <h1>${user.id == null ? 'Add New User' : 'Edit User'}</h1>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form action="/admin/users/save" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="hidden" name="id" value="${user.id}">

        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" class="form-control" id="username" name="username"
                   value="${user.username}" required>
        </div>

        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control" id="password" name="password"
                   placeholder="${user.id == null ? 'Enter password' : 'Leave blank to keep current'}">
        </div>

        <div class="form-group">
            <label for="fullName">Full Name</label>
            <input type="text" class="form-control" id="fullName" name="fullName"
                   value="${user.fullName}" required>
        </div>

        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" class="form-control" id="email" name="email"
                   value="${user.email}" required>
        </div>

        <div class="form-group">
            <label for="phoneNumber">Phone Number</label>
            <input type="text" class="form-control" id="phoneNumber" name="phoneNumber"
                   value="${user.phoneNumber}" required>
        </div>

        <div class="form-group">
            <label for="role">Role</label>
            <select class="form-control" id="role" name="role" required
                    <c:if test="${user.id != null && user.id == currentUser.id}">disabled</c:if>>
                <option value="STUDENT" ${user.role eq 'STUDENT' ? 'selected' : ''}>Student</option>
                <option value="TEACHER" ${user.role eq 'TEACHER' ? 'selected' : ''}>Teacher</option>
            </select>
            <c:if test="${user.id != null && user.id == currentUser.id}">
                <input type="hidden" name="role" value="${user.role}">
                <small class="text-muted">You cannot change your own role</small>
            </c:if>
        </div>

        <button type="submit" class="btn btn-primary">Save</button>
        <a href="/admin/users" class="btn btn-secondary">Cancel</a>
    </form>
</div>
</body>
</html>
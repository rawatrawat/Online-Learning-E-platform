<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit User</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <style>
        .form-container {
            max-width: 600px;
            margin: 30px auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background-color: #f9f9f9;
        }
        .required-field::after {
            content: " *";
            color: red;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="form-container">
        <h2 class="text-center mb-4">Edit User Profile</h2>


        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>


        <form id="editForm" action="/admin/users/update" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <input type="hidden" name="id" value="${user.id}">


            <div class="form-group">
                <label for="username" class="required-field">Username</label>
                <input type="text" class="form-control" id="username"
                       name="username" value="${user.username}" required
                       pattern="[a-zA-Z0-9]{4,20}" title="4-20 letters or numbers">
            </div>


            <div class="form-group">
                <label for="password">New Password</label>
                <input type="password" class="form-control" id="password"
                       name="password" placeholder="Leave blank to keep current"
                       minlength="6" maxlength="20">
                <small class="text-muted">Minimum 6 characters</small>
            </div>


            <div class="form-group">
                <label for="fullName" class="required-field">Full Name</label>
                <input type="text" class="form-control" id="fullName"
                       name="fullName" value="${user.fullName}" required>
            </div>

            <div class="form-group">
                <label for="email" class="required-field">Email</label>
                <input type="email" class="form-control" id="email"
                       name="email" value="${user.email}" required>
            </div>

            <div class="form-group">
                <label for="phoneNumber" class="required-field">Phone Number</label>
                <input type="tel" class="form-control" id="phoneNumber"
                       name="phoneNumber" value="${user.phoneNumber}" required
                       pattern="[0-9]{8,15}" title="8-15 numbers">
            </div>


            <div class="form-group">
                <label for="role" class="required-field">Role</label>
                <select class="form-control" id="role" name="role" required
                        <c:if test="${user.id == currentUser.id}">disabled</c:if>>
                    <option value="STUDENT" ${user.role eq 'STUDENT' ? 'selected' : ''}>Student</option>
                    <option value="TEACHER" ${user.role eq 'TEACHER' ? 'selected' : ''}>Teacher</option>
                </select>
                <c:if test="${user.id == currentUser.id}">
                    <input type="hidden" name="role" value="${user.role}">
                    <small class="text-muted">You cannot change your own role</small>
                </c:if>
            </div>


            <div class="form-group text-center">
                <button type="submit" class="btn btn-primary mr-2">
                    <i class="fas fa-save"></i> Update
                </button>
                <a href="/admin/users" class="btn btn-secondary">
                    <i class="fas fa-times"></i> Cancel
                </a>
            </div>
        </form>
    </div>
</div>


<script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>


<script>
    document.getElementById('editForm').addEventListener('submit', function(e) {
        const password = document.getElementById('password').value;
        if (password && password.length < 6) {
            alert('Password must be at least 6 characters');
            e.preventDefault();
        }
    });
</script>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty param.success}">
    <div class="alert alert-success">${param.success}</div>
</c:if>
<c:if test="${not empty param.error}">
    <div class="alert alert-danger">${param.error}</div>
</c:if>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Course Material</title>
    <style>
        body {
            background-color: lightyellow;
        }
        table {
            width: 100%;
            background-color: lightblue;
            border-collapse: collapse;
        }
        th, td {
            padding: 10px;
            text-align: center;
        }
        .comment-box {
            background-color: lightblue;
            padding: 10px;
            margin-top: 20px;
        }
        .btn-danger {
            background-color: #dc3545;
            color: white;
            border: none;
            padding: 5px 10px;
            border-radius: 3px;
            cursor: pointer;
        }

        .btn-danger:hover {
            background-color: #c82333;
        }

        .comment-box {
            margin-top: 20px;
            padding: 15px;
            background-color: #e9ecef;
            border-radius: 5px;
        }
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border: 1px solid transparent;
            border-radius: 4px;
        }
        .alert-success {
            color: #3c763d;
            background-color: #dff0d8;
            border-color: #d6e9c6;
        }
        .alert-danger {
            color: #a94442;
            background-color: #f2dede;
            border-color: #ebccd1;
        }
    </style>
</head>
<body>
<c:if test="${user.role == 'TEACHER'}">
<div class="course-selector">
    <form method="get" action="/courseMaterial">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <select name="courseId" onchange="this.form.submit()">
            <option value="">-- Select Course --</option>
            <c:forEach items="${allCourses}" var="course">
                <option value="${course.id}"
                        <c:if test="${not empty currentCourse && currentCourse.id == course.id}">selected</c:if>>
                        ${course.name}
                </option>
            </c:forEach>
        </select>
    </form>
</div>
</c:if>
<c:if test="${not empty currentCourse}">
    <center><h1>${currentCourse.name}</h1>
    <p>${currentCourse.description}</p></center>
</c:if>
<a class="nav-link" href="/">Home</a>
<c:if test="${user.role == 'TEACHER'}">
    <a class="manage-btn" href="/courseMaterial/manage?courseId=${courseId}">Manage Lectures</a>
</c:if>
<table>
    <tr>
        <th>Number</th>
        <th>Topic</th>
        <th>Download Lectures</th>
        <th>Download Lab Exercises</th>
    </tr>
    <c:forEach items="${lectures}" var="lecture">
        <tr>
            <td><a href="?courseId=${courseId}&lectureId=${lecture.id}">${lecture.id}</a></td>
            <td>${lecture.topic}</td>
            <td><c:if test="${not empty lecture.lectureFileName}">
                    <a href="/courseMaterial/download/${lecture.id}/lecture">Download Lectures.pdf</a>
                </c:if>
            </td>
            <td>
                <c:if test="${not empty lecture.exerciseFileName}">
                    <a href="/courseMaterial/download/${lecture.id}/exercise">Download Lab Exercise.pdf</a>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>

<c:if test="${not empty lecture}">
    <h2>${lecture.topic}</h2>
    <div>
        <h3>Comments:</h3>
        <c:forEach items="${comments}" var="comment">
            <div style="margin-bottom: 10px; padding: 10px; background-color: #f8f9fa; border-radius: 5px;">
                <p style="margin: 0;"><strong>${comment.user.fullName}</strong> (${comment.timestamp})</p>
                <p style="margin: 0;">${comment.content}</p>
                <c:if test="${not empty user && user.role == 'TEACHER'}">
                    <form action="/courseMaterial/deleteComment" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <input type="hidden" name="commentId" value="${comment.id}">
                        <input type="hidden" name="lectureId" value="${lecture.id}">
                        <input type="hidden" name="courseId" value="${courseId}">
                        <button type="submit" class="btn btn-danger btn-sm"
                                onclick="return confirm('Are you sure?')">
                            Delete
                        </button>
                    </form>
                </c:if>
            </div>
        </c:forEach>
    </div>
    <div class="comment-box">
        <form action="/courseMaterial/comments" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <input type="hidden" name="lectureId" value="${lecture.id}">
            <input type="hidden" name="courseId" value="${courseId}">
            <textarea name="newComment" rows="4" cols="50" placeholder="Add your comment here..."></textarea><br>
            <button type="submit">Submit Comment</button>
        </form>
    </div>
</c:if>
<c:if test="${not empty currentCourse && user.role == 'TEACHER'}">
    <h2>
        <form action="/course/delete" method="post" style="display: inline;">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <input type="hidden" name="courseId" value="${currentCourse.id}">
            <button type="submit" class="btn btn-danger btn-sm"
                    onclick="return confirm('Are you sure you want to delete this Course?')">
                Delete Course
            </button>
        </form>
    </h2>
</c:if>
</div>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Lectures</title>
    <style>
        body {
            background-color: lightyellow;
            font-family: Arial, sans-serif;
        }
        .container {
            width: 80%;
            margin: 0 auto;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        th, td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: lightblue;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
        }
        input[type="text"], input[type="number"] {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
        }
        button {
            padding: 8px 15px;
            background-color: lightblue;
            border: none;
            cursor: pointer;
        }
        button:hover {
            background-color: #add8e6;
        }
        .delete-btn {
            background-color: #ff6b6b;
            color: white;
        }
        .delete-btn:hover {
            background-color: #ff5252;
        }
    </style>
</head>
<body>
<div class="container">
    <a href="/courseMaterial?courseId=${courseId}">Back to Course Materials</a>

    <h2>Add New Lecture</h2>
    <form action="/courseMaterial/add" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="hidden" name="courseId" value="${courseId}" />
        <div class="form-group">
            <label for="id">Lecture ID:</label>
            <input type="number" id="id" name="id" required>
        </div>
        <div class="form-group">
            <label for="topic">Topic:</label>
            <input type="text" id="topic" name="topic" required>
        </div>
        <button type="submit">Add Lecture</button>
    </form>

    <h2>Current Lectures</h2>
    <table>
        <tr>
            <th>ID</th>
            <th>Topic</th>
            <th>Action</th>
        </tr>
        <c:forEach items="${lectures}" var="lecture">
            <tr>
                <td>${lecture.id}</td>
                <td>${lecture.topic}</td>
                <td>
                    <form action="/courseMaterial/delete" method="post" style="display: inline;">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <input type="hidden" name="_method" value="DELETE">
                        <input type="hidden" name="lectureId" value="${lecture.id}">
                        <input type="hidden" name="courseId" value="${courseId}">
                        <button type="submit" class="delete-btn">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
    <h2>Upload Files</h2>
    <form action="/courseMaterial/Upload" method="post" enctype="multipart/form-data">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="hidden" name="courseId" value="${courseId}">
        <div class="form-group">
            <label for="lectureId">Lecture ID:</label>
            <select id="lectureId" name="lectureId" required>
                <c:forEach items="${lectures}" var="lecture">
                    <option value="${lecture.id}">${lecture.id} - ${lecture.topic}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label for="lectureFile">Lecture PDF:</label>
            <input type="file" id="lectureFile" name="lectureFile" accept=".pdf">
        </div>
        <div class="form-group">
            <label for="exerciseFile">Lab Exercise PDF:</label>
            <input type="file" id="exerciseFile" name="exerciseFile" accept=".pdf">
        </div>
        <button type="submit">Upload Files</button>
    </form>
</div>
</body>
</html>

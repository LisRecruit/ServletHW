<%@ page contentType="text/html;charset=UTF8" language="java" %>
<html>
<head>
    <Title>Title</title>
</head>
<body>
<div>
     <h1>Time Zone</h1>
           <p>Selected UTC: ${timeZone}</p>

           <form action="/time" method="POST">
               <label for="timezone">Choose your time zone:</label>
               <select id="timezone" name="timeZone">
                   <%
                       String selectedTimeZone = request.getParameter("timeZone");
                       if (selectedTimeZone == null) {
                           selectedTimeZone = "UTC"; // Установите по умолчанию на "UTC"
                       }

                       for (int i = -12; i <= 12; i++) {
                           String timeZone;
                           if (i >= 0) {
                               timeZone = "UTC+" + i; // Для положительных значений
                           } else {
                               timeZone = "UTC" + i;  // Для отрицательных значений
                           }
                   %>
                           <option value="<%= timeZone %>" <%= (timeZone.equals(selectedTimeZone) ? "selected" : "") %>><%= timeZone %></option>
                   <%
                       }
                   %>
               </select>
               <button type="submit">Submit</button>
           </form>
</div>

</body>
</html>

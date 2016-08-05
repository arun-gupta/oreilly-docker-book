<html>
    <head>
        <title>Hello Docker for Java!</title>
    </head>
    <body>
        <h1>Hello Docker for Java!</h1>
            Hostname: <% out.println(System.getenv("HOSTNAME")); %> <br/>
            Timestamp: <%= new java.util.Date() %>
    </body>
</html>



<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>LUDO Food Delivery</title>

    <style>
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

html, body {
	width: 100%;
	height: 100%;
	overflow: hidden;
	background: #111;
}

.splash-screen {
	width: 100%;
	height: 100%;
	display: flex;
	align-items: center;
	justify-content: center;
}

video {
	position: fixed;
	top: 0;
	left: 0;
	width: 100vw;
	height: 100vh;
	object-fit: cover;
	display: block;
}

.skip-button {
	position: fixed;
	right: 20px;
	bottom: 20px;
	padding: 10px 20px;
	border: none;
	border-radius: 24px;
	background: #ff5a1f;
	color: white;
	cursor: pointer;
}
</style>
</head>

<body>
    <div class="splash-screen">
        <video id="introVideo" autoplay muted playsinline preload="auto">
            <source
                src="<%= request.getContextPath() %>/video/intro_video.mp4"
                type="video/mp4">
        </video>
    </div>

    <button class="skip-button" onclick="openApp()">Skip Intro</button>

    <script>
        const video = document.getElementById("introVideo");
        let navigated = false;

        function openApp() {
            if (navigated) return;
            navigated = true;

            window.location.replace(
                "<%= request.getContextPath() %>/restaurant"
            );
        }

        video.addEventListener("ended", openApp);

        video.addEventListener("error", openApp);
    </script>
</body>
</html>

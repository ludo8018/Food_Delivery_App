<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Food Fiesta Ludo</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/ludo.css">
</head>
<body>

<header class="top">
  <a class="brand" href="<%= request.getContextPath() %>/"><b>LUDO</b><span>Food &amp; Fun</span></a>
  <nav>
    <button id="soundBtn" class="ghost" type="button">🔇 Sound</button>
    <button id="restartBtn" class="ghost" type="button">↻ Restart</button>
  </nav>
</header>

<main>
  <!-- ========== SETUP SCREEN ========== -->
  <section id="setup" class="card setup">
    <small class="kicker">Play LUDO</small>
    <h1>FOOD FIESTA LUDO</h1>
    <p class="tag">Roll the dice. Order food. Let the fun begin!</p>
    <h2>Ready to roll?</h2>
    <p>Choose your colour. Our ludo plays the opposite colour.</p>
    <div class="picks">
      <label class="pick red"><input type="radio" name="col" value="0" checked><span>Red</span></label>
      <label class="pick green"><input type="radio" name="col" value="1"><span>Green</span></label>
      <label class="pick yellow"><input type="radio" name="col" value="2"><span>Yellow</span></label>
      <label class="pick blue"><input type="radio" name="col" value="3"><span>Blue</span></label>
    </div>
    <button id="startBtn" class="cta" type="button">Start game</button>
    <p class="hint">Beat our ludo to unlock a food coupon 🎟️</p>
  </section>

  <!-- ========== GAME SCREEN ========== -->
  <section id="game" hidden>
    <div class="layout">
      <div class="boardwrap"><div id="board" class="board"></div></div>

      <aside class="panel">
        <div class="card">
          <div id="msg" class="msg">Ready!</div>
          <div class="dicerow">
            <div id="dice" class="dice"></div>
            <button id="rollBtn" class="cta" type="button">🎲 Roll dice</button>
          </div>
          <div id="score" class="score"></div>
        </div>

        <!-- Separate coupon box -->
        <section id="coupon" class="card coupon">
          <h3>🎟️ Your Food Coupon</h3>
          <p id="cLocked">Win the game to unlock your coupon code.</p>
          <div id="cWin" hidden>
            <div class="codebox">
              <span id="code" class="code"></span>
              <button id="copyBtn" class="cta small" type="button">Copy</button>
            </div>
            <small>Apply this code at checkout.</small>
          </div>
          <p id="cLose" hidden>No coupon this time. Better luck next time!</p>
        </section>
      </aside>
    </div>
  </section>
</main>

<!-- Result popup -->
<div id="result" class="overlay" hidden>
  <div class="card pop">
    <h2 id="rTitle"></h2>
    <p id="rText"></p>
    <div class="btns">
      <button id="rCoupon" class="cta" type="button">Get my coupon</button>
      <button id="rAgain" class="cta alt" type="button">Play again</button>
    </div>
  </div>
</div>

<script src="<%= request.getContextPath() %>/JS/ludo.js"></script>
</body>
</html>
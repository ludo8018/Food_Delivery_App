/* FOOD FIESTA LUDO - human vs computer. Vanilla JS, no libraries. */
(function () {
  'use strict';

  var N = ['red', 'green', 'yellow', 'blue'];            // clockwise order
  var YO = [[0, 0], [0, 9], [9, 9], [9, 0]];             // yard origins (row, col)
  var SAFE = [0, 8, 13, 21, 26, 34, 39, 47];             // start + star squares (track index)
  var CEN = [[7, 6.35], [6.35, 7], [7, 7.65], [7.65, 7]]; // finished-token spots in centre
  var TRACK = ('6,1 6,2 6,3 6,4 6,5 5,6 4,6 3,6 2,6 1,6 0,6 0,7 0,8 1,8 2,8 3,8 4,8 5,8 ' +
    '6,9 6,10 6,11 6,12 6,13 6,14 7,14 8,14 8,13 8,12 8,11 8,10 8,9 9,8 10,8 11,8 12,8 13,8 14,8 ' +
    '14,7 14,6 13,6 12,6 11,6 10,6 9,6 8,5 8,4 8,3 8,2 8,1 8,0 7,0 6,0')
    .split(' ').map(function (s) { return s.split(',').map(Number); });
  var DOTS = [[4], [0, 8], [0, 4, 8], [0, 2, 6, 8], [0, 2, 4, 6, 8], [0, 2, 3, 5, 6, 8]];
  var CODE_CHARS = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';

  var $ = function (id) { return document.getElementById(id); };
  var boardEl = $('board'), msgEl = $('msg'), rollBtn = $('rollBtn'), diceEl = $('dice');
  var G = null, gid = 0, muted = true, AC = null, lastCol = 0;

  /* ---------- sound ---------- */
  function beep(f, d, type, vol) {
    if (muted) return;
    try {
      AC = AC || new (window.AudioContext || window.webkitAudioContext)();
      var o = AC.createOscillator(), g = AC.createGain();
      o.type = type || 'sine'; o.frequency.value = f; g.gain.value = vol || 0.05;
      o.connect(g); g.connect(AC.destination);
      g.gain.exponentialRampToValueAtTime(0.0001, AC.currentTime + d);
      o.start(); o.stop(AC.currentTime + d);
    } catch (e) { /* audio not available */ }
  }
  function fanfare(win) {
    var s = win ? [523, 659, 784, 1047] : [392, 330, 262];
    s.forEach(function (f, i) { setTimeout(function () { beep(f, 0.25, 'triangle', 0.07); }, i * 180); });
  }

  /* ---------- helpers ---------- */
  function homeCell(ci, k) { return [[7, 1 + k], [1 + k, 7], [7, 13 - k], [13 - k, 7]][ci]; }
  function cellOf(ci, p, t) {
    if (p < 0) { var o = YO[ci]; return [o[0] + 2 + (t >> 1), o[1] + 2 + (t & 1)]; }
    if (p <= 50) return TRACK[(ci * 13 + p) % 52];
    if (p <= 55) return homeCell(ci, p - 51);
    return CEN[ci];
  }
  function place(el, r, c, size) {
    el.style.left = (c * 100 / 15) + '%'; el.style.top = (r * 100 / 15) + '%';
    if (size) { el.style.width = el.style.height = (size * 100 / 15) + '%'; }
  }
  function later(fn, ms) {
    var id = G && G.id;
    setTimeout(function () { if (G && G.id === id) fn(); }, ms);
  }
  function setMsg(t) { msgEl.textContent = t; }
  function trackIdx(ci, p) { return (ci * 13 + p) % 52; }

  /* ---------- dice ---------- */
  for (var i = 0; i < 9; i++) diceEl.appendChild(document.createElement('i'));
  function showDice(v) {
    [].forEach.call(diceEl.children, function (d, k) { d.className = DOTS[v - 1].indexOf(k) > -1 ? 'on' : ''; });
  }
  showDice(1);

  /* ---------- board ---------- */
  function buildBoard() {
    boardEl.innerHTML = '';
    var info = {}, r, c;
    TRACK.forEach(function (rc, k) {
      var o = { c: 'p', t: '' };
      if (k % 13 === 0) { o.c = 'p ' + N[k / 13]; o.t = '★'; }
      else if (SAFE.indexOf(k) > -1) o.t = '★';
      info[rc[0] + ',' + rc[1]] = o;
    });
    for (var ci = 0; ci < 4; ci++) for (var k = 0; k < 5; k++) {
      var h = homeCell(ci, k); info[h[0] + ',' + h[1]] = { c: 'p ' + N[ci], t: '' };
    }
    for (r = 0; r < 15; r++) for (c = 0; c < 15; c++) {
      var d = document.createElement('div'), o = info[r + ',' + c];
      if (o) { d.className = 'cell ' + o.c; d.textContent = o.t; }
      else if (r >= 6 && r <= 8 && c >= 6 && c <= 8) d.className = 'cell';
      else d.className = 'cell y ' + N[r < 6 ? (c < 6 ? 0 : 1) : (c < 6 ? 3 : 2)];
      boardEl.appendChild(d);
    }
    var cen = document.createElement('div'); cen.className = 'center'; boardEl.appendChild(cen);
    YO.forEach(function (o, ci) {
      var b = document.createElement('div'); b.className = 'ybox'; place(b, o[0] + 1, o[1] + 1, 4); boardEl.appendChild(b);
      for (var t = 0; t < 4; t++) {
        var s = document.createElement('div'), yc = cellOf(ci, -1, t);
        s.className = 'spot ' + N[ci]; place(s, yc[0] + 0.1, yc[1] + 0.1); boardEl.appendChild(s);
      }
    });
    G.pl.forEach(function (pl) {
      pl.tk.forEach(function (t) {
        var el = document.createElement('div');
        el.className = 'token ' + N[pl.ci];
        el.addEventListener('click', function () {
          if (G && G.wait && G.wait.mv.indexOf(t) > -1) moveToken(G.pl[0], t, G.wait.v);
        });
        boardEl.appendChild(el); t.el = el;
      });
    });
  }

  function render() {
    var groups = {};
    G.pl.forEach(function (pl) {
      pl.tk.forEach(function (t, ti) {
        if (t.p >= 0 && t.p <= 55) {
          var c = cellOf(pl.ci, t.p, ti), k = c[0] + ',' + c[1];
          (groups[k] = groups[k] || []).push(t);
        }
      });
    });
    G.pl.forEach(function (pl) {
      pl.tk.forEach(function (t, ti) {
        var c = cellOf(pl.ci, t.p, ti), g = groups[c[0] + ',' + c[1]], dc = 0;
        if (t.p >= 0 && t.p <= 55 && g && g.length > 1) dc = (g.indexOf(t) - (g.length - 1) / 2) * 0.22;
        place(t.el, c[0] + 0.1, c[1] + 0.1 + dc);
      });
    });
    $('score').innerHTML = G.pl.map(function (pl, i) {
      var h = pl.tk.filter(function (t) { return t.p === 56; }).length;
      return '<div class="row ' + N[pl.ci] + (i === G.cur && !G.over ? ' act' : '') + '"><i class="dot"></i><b>' +
        (pl.human ? 'You' : 'Computer') + '</b><span>🏠 ' + h + '/4</span></div>';
    }).join('');
  }

  /* ---------- game flow ---------- */
  function mk(ci, human) {
    var tk = []; for (var i = 0; i < 4; i++) tk.push({ p: -1, el: null });
    return { ci: ci, human: human, tk: tk };
  }

  function newGame(ci) {
    lastCol = ci;
    G = { id: ++gid, cur: 0, busy: false, over: false, sixes: 0, wait: null, pl: [mk(ci, true), mk((ci + 2) % 4, false)] };
    $('setup').hidden = true; $('game').hidden = false; $('result').hidden = true;
    resetCoupon(); buildBoard(); showDice(1); render(); turnStart();
  }

  function turnStart() {
    if (G.over) return;
    var pl = G.pl[G.cur];
    G.busy = false;
    diceEl.className = 'dice ' + N[pl.ci];
    rollBtn.disabled = !pl.human;
    setMsg(pl.human ? 'Your turn - roll the dice!' : 'Computer is thinking...');
    render();
    if (!pl.human) later(doRoll, 900);
  }

  function nextTurn() { G.cur = 1 - G.cur; G.sixes = 0; turnStart(); }

  function doRoll() {
    if (G.busy || G.over) return;
    G.busy = true; rollBtn.disabled = true;
    var n = 0, id = G.id;
    (function spin() {
      if (!G || G.id !== id) return;
      if (n++ < 8) { showDice(1 + Math.floor(Math.random() * 6)); beep(260 + n * 25, 0.05, 'square', 0.03); setTimeout(spin, 70); }
      else afterRoll(1 + Math.floor(Math.random() * 6));
    })();
  }

  function movable(pl, v) {
    return pl.tk.filter(function (t) { return (t.p === -1 && v === 6) || (t.p >= 0 && t.p + v <= 56); });
  }

  function afterRoll(v) {
    var pl = G.pl[G.cur], who = pl.human ? 'You' : 'Computer';
    showDice(v); beep(600, 0.08, 'triangle');
    G.sixes = v === 6 ? G.sixes + 1 : 0;
    if (G.sixes === 3) { setMsg(who + ' rolled three 6s in a row - turn lost!'); later(nextTurn, 1300); return; }
    var mv = movable(pl, v);
    if (!mv.length) { setMsg(who + ' rolled ' + v + ' - no move possible.'); later(nextTurn, 1100); return; }
    if (!pl.human) {
      setMsg('Computer rolled ' + v + '.');
      later(function () { moveToken(pl, pick(pl, mv, v), v); }, 800);
    } else if (mv.length === 1) {
      setMsg('You rolled ' + v + '.');
      later(function () { moveToken(pl, mv[0], v); }, 500);
    } else {
      G.wait = { v: v, mv: mv };
      mv.forEach(function (t) { t.el.classList.add('can'); });
      setMsg('You rolled ' + v + ' - tap a glowing token to move.');
    }
  }

  function moveToken(pl, t, v) {
    G.wait = null; G.busy = true;
    pl.tk.forEach(function (x) { x.el.classList.remove('can'); });
    var steps = t.p === -1 ? 1 : v, done = 0, id = G.id;
    (function step() {
      if (!G || G.id !== id) return;
      t.p = t.p === -1 ? 0 : t.p + 1;
      render(); beep(420 + done * 30, 0.06, 'triangle'); done++;
      if (done < steps) setTimeout(step, 200); else setTimeout(land, 160);
    })();

    function land() {
      if (!G || G.id !== id) return;
      var opp = G.pl[1 - G.pl.indexOf(pl)], cap = false;
      if (t.p <= 50) {
        var idx = trackIdx(pl.ci, t.p);
        if (SAFE.indexOf(idx) < 0) {
          opp.tk.forEach(function (o) {
            if (o.p >= 0 && o.p <= 50 && trackIdx(opp.ci, o.p) === idx) { o.p = -1; cap = true; }
          });
        }
      }
      var home = t.p === 56;
      render();
      if (cap) { beep(150, 0.3, 'sawtooth', 0.07); setMsg((pl.human ? 'You captured' : 'Computer captured') + ' a token! Extra turn.'); }
      else if (home) { beep(880, 0.25, 'triangle', 0.07); setMsg((pl.human ? 'Your' : "Computer's") + ' token reached home! Extra turn.'); }
      if (pl.tk.every(function (x) { return x.p === 56; })) { endGame(pl); return; }
      if (v === 6 || cap || home) later(turnStart, 900); else later(nextTurn, 500);
    }
  }

  /* ---------- computer AI ---------- */
  function threat(opp, idx) {
    return opp.tk.some(function (o) {
      if (o.p < 0 || o.p > 50) return false;
      var d = (idx - trackIdx(opp.ci, o.p) + 52) % 52; return d >= 1 && d <= 6;
    });
  }
  function hit(opp, idx) {
    return opp.tk.some(function (o) { return o.p >= 0 && o.p <= 50 && trackIdx(opp.ci, o.p) === idx; });
  }
  function pick(pl, mv, v) {
    var opp = G.pl[1 - G.pl.indexOf(pl)], best = mv[0], bs = -1e9;
    mv.forEach(function (t) {
      var np = t.p < 0 ? 0 : t.p + v, s = np;
      if (t.p < 0) s += 60;
      if (np === 56) s += 120; else if (np > 50) s += 35;
      if (np <= 50) {
        var ni = trackIdx(pl.ci, np);
        if (SAFE.indexOf(ni) > -1) s += 25;
        else if (hit(opp, ni)) s += 200;
        else if (threat(opp, ni)) s -= 30;
      }
      if (t.p >= 0 && t.p <= 50) {
        var oi = trackIdx(pl.ci, t.p);
        if (SAFE.indexOf(oi) < 0 && threat(opp, oi)) s += 30;
      }
      s += Math.random() * 8;
      if (s > bs) { bs = s; best = t; }
    });
    return best;
  }

  /* ---------- end of game + coupon ---------- */
  function makeCode() {
    var code;
    do {
      var a = new Uint8Array(8);
      if (window.crypto && crypto.getRandomValues) crypto.getRandomValues(a);
      else for (var i = 0; i < 8; i++) a[i] = Math.floor(Math.random() * 256);
      code = Array.prototype.map.call(a, function (b) { return CODE_CHARS[b % CODE_CHARS.length]; }).join('');
    } while (!/[A-Z]/.test(code) || !/[2-9]/.test(code));
    return code.slice(0, 4) + '-' + code.slice(4);
  }

  function resetCoupon() {
    $('cLocked').hidden = false; $('cWin').hidden = true; $('cLose').hidden = true;
    $('coupon').className = 'card coupon';
  }

  function endGame(pl) {
    G.over = true; G.busy = true; rollBtn.disabled = true; render();
    if (pl.human) {
      $('code').textContent = makeCode();
      $('cLocked').hidden = true; $('cLose').hidden = true; $('cWin').hidden = false;
      $('coupon').className = 'card coupon unlocked';
      setMsg('🎉 You won!');
      $('rTitle').textContent = '🎉 You won!';
      $('rText').textContent = 'Your food coupon is unlocked in the coupon box.';
      $('rCoupon').hidden = false;
    } else {
      $('cLocked').hidden = true; $('cWin').hidden = true; $('cLose').hidden = false;
      setMsg('Computer won this round.');
      $('rTitle').textContent = 'Better luck next time!';
      $('rText').textContent = 'The computer won this round. Play again for another shot at a coupon.';
      $('rCoupon').hidden = true;
    }
    fanfare(pl.human);
    setTimeout(function () { $('result').hidden = false; }, 700);
  }

  /* ---------- UI events ---------- */
  $('startBtn').addEventListener('click', function () {
    var sel = document.querySelector('input[name="col"]:checked');
    newGame(sel ? +sel.value : 0);
  });
  $('restartBtn').addEventListener('click', function () {
    G = null; $('result').hidden = true; $('game').hidden = true; $('setup').hidden = false;
  });
  $('rAgain').addEventListener('click', function () { newGame(lastCol); });
  $('rCoupon').addEventListener('click', function () {
    $('result').hidden = true;
    var c = $('coupon'); c.scrollIntoView({ behavior: 'smooth', block: 'center' });
    c.classList.add('flash'); setTimeout(function () { c.classList.remove('flash'); }, 1600);
  });
  rollBtn.addEventListener('click', function () { if (G && !G.busy && G.pl[G.cur].human) doRoll(); });
  diceEl.addEventListener('click', function () { rollBtn.click(); });
  $('soundBtn').addEventListener('click', function () {
    muted = !muted; this.textContent = (muted ? '🔇' : '🔊') + ' Sound';
    if (!muted) beep(660, 0.1, 'triangle');
  });

  $('copyBtn').addEventListener('click', function () {
    var btn = this, text = $('code').textContent;
    function ok() { btn.textContent = 'Copied ✓'; setTimeout(function () { btn.textContent = 'Copy'; }, 1500); }
    function fallback() {
      var ta = document.createElement('textarea'); ta.value = text; ta.style.position = 'fixed'; ta.style.opacity = '0';
      document.body.appendChild(ta); ta.select();
      try { document.execCommand('copy'); ok(); } catch (e) { btn.textContent = 'Press Ctrl+C'; }
      document.body.removeChild(ta);
    }
    if (navigator.clipboard && navigator.clipboard.writeText) navigator.clipboard.writeText(text).then(ok, fallback);
    else fallback();
  });
})();

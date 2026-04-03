import './main.css';
import { defineComponent as N, onMounted as V, onUnmounted as q, ref as Y, reactive as j, watch as K, computed as A, openBlock as _, createBlock as ee, Teleport as te, createElementVNode as i, normalizeClass as w, withModifiers as O, normalizeStyle as m, unref as h, toDisplayString as P } from "vue";
const ae = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAAELklEQVRYR+2YW2wUZRTH//9vtlCoF9IoIklT3PqgPGi326hoetuaGEhIr9SgCYkkgt2WGOQVCca+GavWdr0GjD4YhG3RB3hply1LQA1tEQIxEXapGI2pEkys9LIzx2ylYWfY6e5sF0oi+7hzzvl+3/9855xvhrjNf7zN+XAHcL4Z+n8o6JWTeYt++W25S596AIZy6TB+n3yo+Nchlk8vmIIVowdXU9c3Q1gDSilBlQwjgBAYFGDvdF58/4milqvZwDpOcXWsb5Uh8hmBqkwXFMhlCN8aX5LXNbRy/T+Z+iXsHAFWRXs3QGQPyLucLDJrK5DgUXdTsxPfjAEro8E3Ce50EtxsKxPTwCPH3U2jTmJkBJgWTnAMxDeGMEoa0xQ+LJQnCD4HYFkCyAC3RdwN3U7gMkpxRTTYrMD91sCJIgCxV5R6O1Jcfy7VwonqLoj9/CqB2kF341qncGkBvRe+ureAWpRgoalCBecMFzcdK24YymZRJz5zprgq1tsJwXYL3CVZGvdGHmwZc7JQtra2gE+f712ep2QUYP714DJhaJrXLqXZQszlZwtYdSHoB9ljVk/ePVrSZFL0ZkAlxzQBVseCT8WhZhRThtFB8plk9Zi/qCi8cv0fNxvKFrDy4oF11NXXIFy2EII4iBcG3Y03VLZT8OqRd5aFPduvOEpxRayvXolxAKB2g6NgEhobBlc1HHYKY7WvHf5wtVAPgegIlbbZ9seUZ7AyFnwewi9pGoUyDmhrB931kfnC1ZwOeKlLP8GZJi6QLSFP2yep4toXSbT3ZQAfX3O6omt8Nhd9r/aHQAUMOQywYBZo5uZD2ThQ2rbPCjlnH6yI9rUryE5DU75ctJaake46Be4DuDjF8dFBNA94/AdtiySVxIlpMlTS8td801o70vMigM9huTda2lhcKHVHPO2HZv/P6LIwX7hk/+qzPSvUJGMkrg8AQYTkroRdXMlE+HH/twsG6BsOdJHYZlaO/lBZ6weOiiSXqs3Gqj0TeAxx+T75DIpgwjC0onD51pQD4JaluPrkR/cpFT9DcoVp84LOgTL/DjtBbglgou+puHwB8lEznPxJw1XSX77VtgizBvQNBw4RMqB7xt4Lc3c8lQKJaQHoO4R8ydz0/7MWoCXk8c85MrMC9J3qaafw/WtQlwXST+F3BnAeYB4obgJ1BJIuG+YtiKAjVOZ/Pd1ZdwzoG+4uBtSPpjaRbhXLcwF3hzytb2TilgVgT5BkYybBrTYC+Rvg5nRpdTRJrIs8+VPXPQXj2i4ItxC4O2NQQUQnN4U9rRcz9nH64p4ceM2lziX5Y4s3KHCdUHwE77ecMkMEp6BwhIa2Z6DslZRvfulgHafYLuCas58WLp2aLCFUga70qxOFU6dPFL2W1feYeaU43Y5z/TxnCuYabMEuC043ckdBp4pZ7f8FE5psOI1g6fwAAAAASUVORK5CYII=", se = ["width", "height"], ne = ["width", "height"], ie = ["width", "height"], oe = /* @__PURE__ */ i("div", { class: "loading-gif_" }, [
  /* @__PURE__ */ i("span"),
  /* @__PURE__ */ i("span"),
  /* @__PURE__ */ i("span"),
  /* @__PURE__ */ i("span"),
  /* @__PURE__ */ i("span")
], -1), le = [
  oe
], ce = { class: "auth-control_" }, re = { class: "range-text" }, de = /* @__PURE__ */ i("div", null, null, -1), he = /* @__PURE__ */ i("div", null, null, -1), ue = /* @__PURE__ */ i("div", null, null, -1), fe = [
  de,
  he,
  ue
], ge = /* @__PURE__ */ N({
  __name: "App",
  props: {
    canvasWidth: { type: Number, default: 310 },
    // 主canvas的宽
    canvasHeight: { type: Number, default: 160 },
    // 主canvas的高
    show: { type: Boolean, default: !1 },
    // 是否出现，由父级控制
    puzzleScale: { type: Number, default: 1 },
    // 拼图块的大小缩放比例
    sliderSize: { type: Number, default: 50 },
    // 滑块的大小
    range: { type: Number, default: 10 },
    // 允许的偏差值
    imgs: {
      type: Array,
      default: null
    },
    successText: {
      type: String,
      default: "验证通过！"
    },
    failText: {
      type: String,
      default: "验证失败，请重试"
    },
    sliderText: {
      type: String,
      default: "拖动滑块完成拼图"
    }
  },
  emits: ["success", "fail", "close"],
  setup(l, { emit: B }) {
    const n = l;
    V(() => {
      document.addEventListener("mousemove", x, !1), document.addEventListener("mouseup", S, !1), document.addEventListener("touchmove", x, {
        passive: !1
      }), document.addEventListener("touchend", S, !1), n.show && (document.body.classList.add("vue-puzzle-overflow"), T());
    }), q(() => {
      e.timer1 && clearTimeout(e.timer1), document.removeEventListener("mousemove", x, !1), document.removeEventListener("mouseup", S, !1), document.removeEventListener("touchmove", x), document.removeEventListener("touchend", S, !1);
    });
    const W = Y(), H = Y(), z = Y(), D = Y(), e = j({
      mouseDown: !1,
      startWidth: 50,
      startX: 0,
      newX: 0,
      pinX: 0,
      pinY: 0,
      loading: !1,
      isCanSlide: !1,
      error: !1,
      infoBoxShow: !1,
      infoText: "",
      infoBoxFail: !1,
      timer1: void 0,
      closeDown: !1,
      isSuccess: !1,
      imgIndex: -1,
      isSubmting: !1
    });
    K(
      () => n.show,
      (s) => {
        s ? (document.body.classList.add("vue-puzzle-overflow"), T()) : (e.isSubmting = !1, e.isSuccess = !1, e.infoBoxShow = !1, document.body.classList.remove("vue-puzzle-overflow"));
      }
    );
    const X = A(() => {
      const s = e.startWidth + e.newX - e.startX;
      return s < r.value ? r.value : s > n.canvasWidth ? n.canvasWidth : s;
    }), c = A(() => Math.round(Math.max(Math.min(n.puzzleScale, 2), 0.2) * 52.5 + 6)), r = A(() => Math.max(
      Math.min(Math.round(n.sliderSize), Math.round(n.canvasWidth * 0.5)),
      10
    )), E = () => {
      !e.mouseDown && !e.isSubmting && (e.timer1 && clearTimeout(e.timer1), B("close"));
    }, I = () => {
      e.closeDown = !0;
    }, R = () => {
      e.closeDown && E(), e.closeDown = !1;
    }, k = (s) => {
      var t;
      e.isCanSlide && (e.mouseDown = !0, e.startWidth = ((t = W.value) == null ? void 0 : t.clientWidth) ?? 0, e.newX = s.clientX || s.changedTouches[0].clientX, e.startX = s.clientX || s.changedTouches[0].clientX);
    }, x = (s) => {
      e.mouseDown && (s.preventDefault(), e.newX = s.clientX || s.changedTouches[0].clientX);
    }, S = () => {
      e.mouseDown && (e.mouseDown = !1, U());
    }, L = (s = !1) => {
      var F;
      if (e.loading && !s)
        return;
      e.loading = !0, e.isCanSlide = !1;
      const t = H.value, d = z.value, u = D.value, a = t == null ? void 0 : t.getContext("2d", { willReadFrequently: !0 }), g = d == null ? void 0 : d.getContext("2d"), f = u == null ? void 0 : u.getContext("2d");
      if (!a || !g || !f) {
        console.error("not found ctx / ctx2 / ctx3");
        return;
      }
      const $ = navigator.userAgent.indexOf("Firefox") >= 0 && navigator.userAgent.indexOf("Windows") >= 0, p = document.createElement("img");
      if (a.fillStyle = "rgba(255,255,255,1)", f.fillStyle = "rgba(255,255,255,1)", a.clearRect(0, 0, n.canvasWidth, n.canvasHeight), g.clearRect(0, 0, n.canvasWidth, n.canvasHeight), e.pinX = o(
        c.value + 20,
        n.canvasWidth - c.value - 10
      ), e.pinY = o(20, n.canvasHeight - c.value - 10), p.crossOrigin = "anonymous", p.onload = () => {
        const [v, C, M, b] = Z(p);
        a.save(), y(a), a.closePath(), $ ? (a.clip(), a.save(), a.shadowOffsetX = 0, a.shadowOffsetY = 0, a.shadowColor = "#000", a.shadowBlur = 3, a.fill(), a.restore()) : (a.shadowOffsetX = 0, a.shadowOffsetY = 0, a.shadowColor = "#000", a.shadowBlur = 3, a.fill(), a.clip()), a.drawImage(p, v, C, M, b), f.fillRect(0, 0, n.canvasWidth, n.canvasHeight), f.drawImage(p, v, C, M, b), a.globalCompositeOperation = "source-atop", y(a), a.arc(
          e.pinX + Math.ceil(c.value / 2),
          e.pinY + Math.ceil(c.value / 2),
          c.value * 1.2,
          0,
          Math.PI * 2,
          !0
        ), a.closePath(), a.shadowColor = "rgba(255, 255, 255, .8)", a.shadowOffsetX = -1, a.shadowOffsetY = -1, a.shadowBlur = Math.min(Math.ceil(8 * n.puzzleScale), 12), a.fillStyle = "#ffffaa", a.fill();
        const G = a.getImageData(
          e.pinX - 3,
          // 为了阴影 是从-3px开始截取，判定的时候要+3px
          e.pinY - 20,
          e.pinX + c.value + 5,
          e.pinY + c.value + 5
        );
        g.putImageData(G, 0, e.pinY - 20), a.restore(), a.clearRect(0, 0, n.canvasWidth, n.canvasHeight), a.save(), y(a), a.globalAlpha = 0.8, a.fillStyle = "#ffffff", a.fill(), a.restore(), a.save(), a.globalCompositeOperation = "source-atop", y(a), a.arc(
          e.pinX + Math.ceil(c.value / 2),
          e.pinY + Math.ceil(c.value / 2),
          c.value * 1.2,
          0,
          Math.PI * 2,
          !0
        ), a.shadowColor = "#000", a.shadowOffsetX = 2, a.shadowOffsetY = 2, a.shadowBlur = 16, a.fill(), a.restore(), a.save(), a.globalCompositeOperation = "destination-over", a.drawImage(p, v, C, M, b), a.restore(), e.loading = !1, e.isCanSlide = !0;
      }, p.onerror = () => {
        L(!0);
      }, !s && ((F = n.imgs) != null && F.length)) {
        let v = o(0, n.imgs.length - 1);
        v === e.imgIndex && (v === n.imgs.length - 1 ? v = 0 : v++), e.imgIndex = v, p.src = n.imgs[v];
      } else
        p.src = Q();
    }, o = (s, t) => Math.ceil(Math.random() * (t - s) + s), Z = (s) => {
      const t = s.width / s.height, d = n.canvasWidth / n.canvasHeight;
      let u = 0, a = 0, g = 0, f = 0;
      return t > d ? (f = n.canvasHeight, g = t * f, a = 0, u = (n.canvasWidth - g) / 2) : (g = n.canvasWidth, f = g / t, u = 0, a = (n.canvasHeight - f) / 2), [u, a, g, f];
    }, y = (s) => {
      const t = Math.ceil(15 * n.puzzleScale);
      s.beginPath(), s.moveTo(e.pinX, e.pinY), s.lineTo(e.pinX + t, e.pinY), s.arcTo(
        e.pinX + t,
        e.pinY - t / 2,
        e.pinX + t + t / 2,
        e.pinY - t / 2,
        t / 2
      ), s.arcTo(
        e.pinX + t + t,
        e.pinY - t / 2,
        e.pinX + t + t,
        e.pinY,
        t / 2
      ), s.lineTo(e.pinX + t + t + t, e.pinY), s.lineTo(e.pinX + t + t + t, e.pinY + t), s.arcTo(
        e.pinX + t + t + t + t / 2,
        e.pinY + t,
        e.pinX + t + t + t + t / 2,
        e.pinY + t + t / 2,
        t / 2
      ), s.arcTo(
        e.pinX + t + t + t + t / 2,
        e.pinY + t + t,
        e.pinX + t + t + t,
        e.pinY + t + t,
        t / 2
      ), s.lineTo(
        e.pinX + t + t + t,
        e.pinY + t + t + t
      ), s.lineTo(e.pinX, e.pinY + t + t + t), s.lineTo(e.pinX, e.pinY + t + t), s.arcTo(
        e.pinX + t / 2,
        e.pinY + t + t,
        e.pinX + t / 2,
        e.pinY + t + t / 2,
        t / 2
      ), s.arcTo(
        e.pinX + t / 2,
        e.pinY + t,
        e.pinX,
        e.pinY + t,
        t / 2
      ), s.lineTo(e.pinX, e.pinY);
    }, Q = () => {
      const s = document.createElement("canvas"), t = s.getContext("2d");
      if (!t)
        return console.error("not found ctx"), "";
      s.width = n.canvasWidth, s.height = n.canvasHeight, t.fillStyle = `rgb(${o(100, 255)},${o(
        100,
        255
      )},${o(100, 255)})`, t.fillRect(0, 0, n.canvasWidth, n.canvasHeight);
      for (let d = 0; d < 12; d++)
        if (t.fillStyle = `rgb(${o(100, 255)},${o(
          100,
          255
        )},${o(100, 255)})`, t.strokeStyle = `rgb(${o(100, 255)},${o(
          100,
          255
        )},${o(100, 255)})`, o(0, 2) > 1)
          t.save(), t.rotate(o(-90, 90) * Math.PI / 180), t.fillRect(
            o(-20, s.width - 20),
            o(-20, s.height - 20),
            o(10, s.width / 2 + 10),
            o(10, s.height / 2 + 10)
          ), t.restore();
        else {
          t.beginPath();
          const u = o(-Math.PI, Math.PI);
          t.arc(
            o(0, s.width),
            o(0, s.height),
            o(10, s.height / 2 + 10),
            u,
            u + Math.PI * 1.5
          ), t.closePath(), t.fill();
        }
      return s.toDataURL("image/png");
    }, U = () => {
      e.isSubmting = !0;
      const s = Math.abs(
        e.pinX - (X.value - r.value) + (c.value - r.value) * ((X.value - r.value) / (n.canvasWidth - r.value)) - 3
      );
      s < n.range ? (e.infoText = n.successText, e.infoBoxFail = !1, e.infoBoxShow = !0, e.isCanSlide = !1, e.isSuccess = !0, e.timer1 && clearTimeout(e.timer1), e.timer1 = setTimeout(() => {
        e.isSubmting = !1, B("success", s);
      }, 800)) : (e.infoText = n.failText, e.infoBoxFail = !0, e.infoBoxShow = !0, e.isCanSlide = !1, B("fail", s), e.timer1 && clearTimeout(e.timer1), e.timer1 = setTimeout(() => {
        e.isSubmting = !1, T();
      }, 800));
    }, J = () => {
      e.infoBoxFail = !1, e.infoBoxShow = !1, e.isCanSlide = !1, e.isSuccess = !1, e.startWidth = r.value, e.startX = 0, e.newX = 0;
    }, T = () => {
      e.isSubmting || (J(), L());
    };
    return (s, t) => (_(), ee(te, { to: "body" }, [
      i("div", {
        class: w(["vue-puzzle-vcode", { show_: l.show }]),
        onMousedown: I,
        onMouseup: R,
        onTouchstart: I,
        onTouchend: R
      }, [
        i("div", {
          class: "vue-auth-box_",
          onMousedown: t[2] || (t[2] = O(() => {
          }, ["stop"])),
          onTouchstart: t[3] || (t[3] = O(() => {
          }, ["stop"]))
        }, [
          i("div", {
            class: "auth-body_",
            style: m(`height: ${l.canvasHeight}px`)
          }, [
            i("canvas", {
              ref_key: "canvas1",
              ref: H,
              width: l.canvasWidth,
              height: l.canvasHeight,
              style: m(`width:${l.canvasWidth}px;height:${l.canvasHeight}px`)
            }, null, 12, se),
            i("canvas", {
              ref_key: "canvas3",
              ref: D,
              class: w(["auth-canvas3_", { show: e.isSuccess }]),
              width: l.canvasWidth,
              height: l.canvasHeight,
              style: m(`width:${l.canvasWidth}px;height:${l.canvasHeight}px`)
            }, null, 14, ne),
            i("canvas", {
              ref_key: "canvas2",
              ref: z,
              class: "auth-canvas2_",
              width: h(c),
              height: l.canvasHeight,
              style: m(`width:${h(c)}px;height:${l.canvasHeight}px;transform:translateX(${h(X) - h(r) - (h(c) - h(r)) * ((h(X) - h(r)) / (l.canvasWidth - h(r)))}px)`)
            }, null, 12, ie),
            i("div", {
              class: w(["loading-box_", { hide_: !e.loading }])
            }, le, 2),
            i("div", {
              class: w([
                "info-box_",
                { show: e.infoBoxShow },
                { fail: e.infoBoxFail }
              ])
            }, P(e.infoText), 3),
            i("div", {
              class: w(["flash_", { show: e.isSuccess }]),
              style: m(`transform: translateX(${e.isSuccess ? `${l.canvasWidth + l.canvasHeight * 0.578}px` : `-${l.canvasHeight * 0.578}px`}) skew(-30deg, 0);`)
            }, null, 6),
            i("img", {
              class: "reset_",
              onClick: T,
              src: ae
            })
          ], 4),
          i("div", ce, [
            i("div", {
              class: "range-box",
              style: m(`height:${h(r)}px`)
            }, [
              i("div", re, P(l.sliderText), 1),
              i("div", {
                class: "range-slider",
                ref_key: "rangeSlider",
                ref: W,
                style: m(`width:${h(X)}px`)
              }, [
                i("div", {
                  class: w(["range-btn", { isDown: e.mouseDown }]),
                  style: m(`width:${h(r)}px`),
                  onMousedown: t[0] || (t[0] = (d) => k(d)),
                  onTouchstart: t[1] || (t[1] = (d) => k(d))
                }, fe, 38)
              ], 4)
            ], 4)
          ])
        ], 32)
      ], 34)
    ]));
  }
});
export {
  ge as default
};

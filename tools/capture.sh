#!/usr/bin/env bash
# Screenshots of the demo build for the READMEs: tools/capture.sh <out-dir> en|ru
# Build it with `./gradlew assembleDebug -Pdemo -Pshots` (app.lantern.shots, never someone's demo):
# the glass doesn't flicker there, so uiautomator can read the screen. The tale's own screen ticks
# every second, so its buttons are tapped where they always are.
set -e
cd "$(dirname "$0")/.."
source tools/phone.sh
OUT="$1"; LANG_="${2:-en}"
PKG=app.lantern.shots
mkdir -p "$OUT"
SHOTS="$OUT"
case "$PKG" in *.demo|*.shots) ;; *) echo "refusing to clear $PKG"; exit 1 ;; esac
"$ADB" shell pm clear $PKG >/dev/null
"$ADB" shell cmd locale set-app-locales $PKG --locales "$LANG_"
"$ADB" shell monkey -p $PKG -c android.intent.category.LAUNCHER 1 >/dev/null 2>&1
sleep 12
if [ "$LANG_" = ru ]; then
  T_LIGHT="Зажечь фонарь"; T_TELL="Рассказывать"; T_ONCE="Жили-были…"; T_SHELF="Просто на полку"
else
  T_LIGHT="Light the lantern"; T_TELL="Tell the tale"; T_ONCE="Once upon a time…"; T_SHELF="Just put it on the shelf"
fi
up() { guard $PKG && "$ADB" shell input swipe 540 1900 540 300 400; sleep 1; }
back() { guard $PKG && "$ADB" shell input keyevent 4; sleep 2; }
shot 1-lantern
up; up
tap_text $PKG "$T_LIGHT"; sleep 6; shot 2-glass
up
tap_text $PKG "$T_TELL"; sleep 3; shot 3-before
tap_text $PKG "$T_ONCE"; sleep 4
tap_at $PKG 284 2234; sleep 4; shot 4-twist
tap_at $PKG 795 2242; sleep 5; shot 5-ending
up
tap_text $PKG "$T_SHELF"; sleep 4; shot 6-tale
back
tap_at $PKG 408 2260; sleep 3; shot 7-shelf
tap_at $PKG 670 2260; sleep 3; shot 8-world
tap_at $PKG 933 2260; sleep 3; shot 9-box
tap_at $PKG 146 2260; sleep 1
echo done

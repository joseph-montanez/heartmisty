all:
	ant debug
	~/android-sdk-linux_86/platform-tools/adb -e install -r bin/HeartMisty-debug.apk
	~/android-sdk-linux_86/platform-tools/adb shell am start -a android.intent.action.MAIN -n com.gorilla3d.app/.HeartMisty

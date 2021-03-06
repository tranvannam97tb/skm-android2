CP := cp
CD := cd
RM := rm -rf
AFPLAY := afplay
SOUND_FILE := /System/Library/Sounds/Glass.aiff
ZIPARCHIVE := zip -r -q

all: clean buildall sound

sound:
	${AFPLAY} ${SOUND_FILE}

beta:
	./gradlew clean assembleBeta

debug:
	./gradlew clean assembleDebug

release:
	./gradlew clean assembleRelease

buildall:
	./gradlew clean assemble

	($(CD) ./app/build/outputs/;$(ZIPARCHIVE) mapping.zip ./mapping/)

clean:
	$(RM) ./app/build/outputs/*

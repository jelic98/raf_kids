#!/bin/bash
if [ $# -eq 1 ]; then
	cd 'SillyGit/src' \
	&& javac -d '../out/production/SillyGit' 'app/ServentSingle.java' \
	&& cd ../.. \
	&& java -cp 'SillyGit/out/production/SillyGit' \
				'app.ServentSingle' \
				'SillyGit/res/test/app.properties' \
				$1
else
	echo "[FAIL] No servent number provided"
fi

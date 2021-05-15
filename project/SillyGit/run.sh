#!/bin/bash
if [ $# -eq 1 ]; then
	clear \
	&& cd 'src' \
	&& javac -d '../out/production/SillyGit' 'app/ServentSingle.java' \
	&& cd .. \
	&& java -cp 'out/production/SillyGit' \
				'app.ServentSingle' \
				'res/app.properties' \
				$1
else
	echo "[FAIL] No servent number provided"
fi

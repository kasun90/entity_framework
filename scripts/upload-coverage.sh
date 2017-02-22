#!/bin/bash

echo "Uploading coverage reports."
files=($(ls */*/target/site/jacoco/jacoco.xml))
for file in "${files[@]}"
do
    echo "Uploading ${file}"
    ~/jpm/bin/codacy-coverage-reporter -l Java -r ${file}
done
echo "Uploading completed."
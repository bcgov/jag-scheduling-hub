#!/usr/bin/env bash
echo " "
echo =============================== Coverage summary ===============================
awk -F"," '{ instructions += $4 + $5; covered += $5 } END  { print "Statements   : " 100*covered/instructions "% ( " covered "/" instructions " )" }' target/site/jacoco/jacoco.csv
awk -F"," '{ instructions += $6 + $7; covered += $7 } END  { print "Branches     : " 100*covered/instructions "% ( " covered "/" instructions " )" }' target/site/jacoco/jacoco.csv
awk -F"," '{ instructions += $12 + $13; covered += $13 } END  { print "Functions    : " 100*covered/instructions "% ( " covered "/" instructions " )" }' target/site/jacoco/jacoco.csv
awk -F"," '{ instructions += $8 + $9; covered += $9 } END  { print "Lines        : " 100*covered/instructions "% ( " covered "/" instructions " )" }' target/site/jacoco/jacoco.csv
echo ================================================================================
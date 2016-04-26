#!/usr/bin/groovy

node {
  def envProd = 'shiftwork-production'

  checkout scm

  stage 'Canary release'
  if (!fileExists ('Dockerfile')) {
    writeFile file: 'Dockerfile', text: 'FROM rhscl/php-56-rhel7'
  }

  def newVersion = "1.0"
  retry(10) {
    newVersion = performCanaryRelease {}
  }

  stage 'Rolling upgrade Production'

}

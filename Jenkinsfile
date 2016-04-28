#!/usr/bin/groovy

def utils = new io.fabric8.Utils()

node {
  def envProd = 'shiftwork-production'

  checkout scm

  stage 'Canary release'
  if (!fileExists ('Dockerfile')) {
    writeFile file: 'Dockerfile', text: 'FROM rhscl/php-56-rhel7'
  }

  def newVersion = "1.0"
  retry(100) {
    newVersion = performCanaryRelease {}
  }

  def rc = getKubernetesJson {
    port = 8080
    label = 'staffservice'
    icon = 'https://cdn.rawgit.com/fabric8io/fabric8/dc05040/website/src/images/logos/nodejs.svg'
    version = newVersion
    imageName = clusterImageName
  }

  stage 'Rolling upgrade Production'
  kubernetesApply(file: rc, environment: envProd)
}

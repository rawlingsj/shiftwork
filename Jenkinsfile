#!/usr/bin/groovy
@Library('github.com/fabric8io/fabric8-pipeline-library@master')

def failIfNoTests = ""
try {
  failIfNoTests = ITEST_FAIL_IF_NO_TEST
} catch (Throwable e) {
  failIfNoTests = "false"
}

def itestPattern = ""
try {
  itestPattern = ITEST_PATTERN
} catch (Throwable e) {
  itestPattern = "*KT"
}

def versionPrefix = ""
try {
  versionPrefix = VERSION_PREFIX
} catch (Throwable e) {
  versionPrefix = "1.0"
}

def canaryVersion = "${versionPrefix}.${env.BUILD_NUMBER}"
//def utils = new io.fabric8.Utils()


def envProd = 'shiftwork-production'

dockerTemplate { //This will ensure that docker socket is mounted and related env vars set.
    mavenNode {  //This will ensure maven container is available and properlly configured.

    checkout scm

    stage 'Canary Release'
    container(name: 'maven') {
      version = canaryVersion
    }



    stage 'Rolling Upgrade Production'
    def rc = readFile 'target/classes/kubernetes.json'
    kubernetesApply(file: rc, environment: envProd)

  }
}

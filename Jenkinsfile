#!/usr/bin/groovy
@Library('github.com/fabric8io/fabric8-pipeline-library@master')

def failIfNoTests = ""
try {
  failIfNoTests = ITEST_FAIL_IF_NO_TEST
} catch (Throwable e) {
  failIfNoTests = "false"
}

def localItestPattern = ""
try {
  localItestPattern = ITEST_PATTERN
} catch (Throwable e) {
  localItestPattern = "*KT"
}


def versionPrefix = ""
try {
  versionPrefix = VERSION_PREFIX
} catch (Throwable e) {
  versionPrefix = "1.0"
}

def canaryVersion = "${versionPrefix}.${env.BUILD_NUMBER}"
def utils = new io.fabric8.Utils()
def label = "buildpod.${env.JOB_NAME}.${env.BUILD_NUMBER}".replace('-', '_').replace('/', '_')

mavenNode{
  def envStage = utils.environmentNamespace('shiftwork-dev')

  git = git branch: 'feature/10-endpoint-for-updates', credentialsId: 'shiftwork', url: 'https://gitlab.com/hughestech/staffservice.git'



  echo 'NOTE: running pipelines for the first time will take longer as build and base docker images are pulled onto the node'
  container(name: 'maven') {

    stage 'Build Release'
    mavenCanaryRelease {
      version = canaryVersion
      sh "mvn fabric8:push -Ddocker.push.registry=fabric8-docker-registry.default.openshift.hughestech.co -e"
      
    }

    stage 'Rollout Staging'
    kubernetesApply(environment: envStage)

  }
}
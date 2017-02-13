#!/usr/bin/groovy
@Library('github.com/rawlingsj/fabric8-pipeline-library@issue')
def versionPrefix = ""
try {
  versionPrefix = VERSION_PREFIX
} catch (Throwable e) {
  versionPrefix = "1.0"
}
def canaryVersion = "${versionPrefix}.${env.BUILD_NUMBER}"
def utils = new io.fabric8.Utils()
def currentNamespace = utils.getNamespace()
def envStage = utils.environmentNamespace('shiftwork-dev')
echo 'NOTE: running pipelines for the first time will take longer as build and base docker images are pulled onto the node'
clientsTemplate{
  jhipsterNode{
    checkout scm
    
    container(name: 'jhipster') {
      // not sure if we need these but they run tests including phantomjs so it's' maybe worth it
      stage 'Build'
      sh 'npm install'
      sh 'bower install --allow-root'   

      stage 'Canary Release Staging'
      mavenCanaryRelease {
        version = canaryVersion
      }
    }
    stage "Rollout ${envStage}"
    kubernetesApply(environment: envStage)

    container(name: 'clients') {
      sh "oc tag ${currentNamespace}/shiftwork:${canaryVersion} ${envStage}/shiftwork:${canaryVersion}"
    }
  }
}
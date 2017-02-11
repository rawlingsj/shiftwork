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
def envStage = utils.environmentNamespace('shiftwork-dev')
echo 'NOTE: running pipelines for the first time will take longer as build and base docker images are pulled onto the node'
jhipsterNode{
  checkout scm
    container(name: 'jhipster') {
      // not sure if we need these but they run tests including phantomjs so it's' maybe worth it
      stage 'Build'
      sh 'npm install'
      sh 'bower install --allow-root'  
        
      def s2iMode = flow.isOpenShiftS2I()
      echo "s2i mode: ${s2iMode}"

      stage 'Canary Release Staging'
      mavenCanaryRelease {
        version = canaryVersion
      }
    }
    stage "Rollout ${envStage}"
    kubernetesApply(environment: envStage, registry: '')

}

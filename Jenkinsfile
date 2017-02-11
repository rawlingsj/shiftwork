#!/usr/bin/groovy
@Library('github.com/rawlingsj/fabric8-pipeline-library@issue')
def versionPrefix = ""
try {
  versionPrefix = VERSION_PREFIX
} catch (Throwable e) {
  versionPrefix = "1.0"
}
def flow = new io.fabric8.Fabric8Commands()
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
    
    
    if (flow.isSingleNode()){
        echo "in flow.isSingleNode()"
        echo 'Running on a single node, skipping docker push as not needed'
        def m = readMavenPom file: 'pom.xml'
        def groupId = m.groupId.split( '\\.' )
        def user = groupId[groupId.size()-1].trim()
        def artifactId = m.artifactId

       if (!s2iMode) {
           echo "in if (!s2iMode)"
           sh "docker tag ${user}/${artifactId}:${config.version} ${env.FABRIC8_DOCKER_REGISTRY_SERVICE_HOST}:${env.FABRIC8_DOCKER_REGISTRY_SERVICE_PORT}/${user}/${artifactId}:${config.version}"
       }
    } else {
      if (!s2iMode) {
        retry(3){
            echo "in !flow.isSingleNode() AND if (!s2iMode)"
            sh "mvn fabric8:push -Ddocker.push.registry=${env.FABRIC8_DOCKER_REGISTRY_SERVICE_HOST}:${env.FABRIC8_DOCKER_REGISTRY_SERVICE_PORT}"
        }
      }
    }

    if (flow.hasService("content-repository")) {
      try {
        sh 'mvn site site:deploy'
      } catch (err) {
        // lets carry on as maven site isn't critical
        echo 'unable to generate maven site'
      }
    } else {
      echo 'no content-repository service so not deploying the maven site report'
    }
    
    //kubernetesApply(environment: envStage, registry: '')
    
    

}

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
echo "DOCKER_HOST is :${env.DOCKER_HOST}"

echo 'NOTE: running pipelines for the first time will take longer as build and base docker images are pulled onto the node'

withEnv(['DOCKER_CONFIG=/home/jenkins/.docker/', 'DOCKER_HOST=unix:/var/run/docker.sock']) {
 {
    
    
jhipsterNode{
  checkout scm
    container(name: 'jhipster', envVars: [ 	containerEnvVar(key: 'DOCKER_CONFIG', value: '/home/jenkins/.docker/')] ) {
        
               
        //Check if ennvars are actually set
        echo "DOCKER_CONFIG is :${env.DOCKER_CONFIG}"
        echo "DOCKER_HOST inside container() is :${env.DOCKER_HOST}"
        
      def flow = new io.fabric8.Fabric8Commands()
      def s2iMode = flow.isOpenShiftS2I()
      // not sure if we need these but they run tests including phantomjs so it's' maybe worth it
      stage 'Build'
      sh 'npm install'
      sh 'bower install --allow-root'  
        
        
      stage "Rollout ${envStage}"
    
      echo "s2i mode: ${s2iMode}"
    
    if (flow.isSingleNode()){
        echo "in flow.isSingleNode()"
        echo 'Running on a single node, skipping docker push as not needed'
        def m = readMavenPom file: 'pom.xml'
        def groupId = m.groupId.split( '\\.' )
        def user = groupId[groupId.size()-1].trim()
        def artifactId = m.artifactId

       if (!s2iMode) {
           echo "in if (!s2iMode)"
           sh "docker tag hughestech/staffservice:version 172.30.139.137:5000/hughestech/staffservice:version"
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
        
      

      stage 'Canary Release Staging'
      mavenCanaryRelease {
        version = canaryVersion
      }
    }
    
    
    //kubernetesApply(environment: envStage, registry: '')
    
    

}

}

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

node {
  def envProd = 'shiftwork-production'

  checkout scm

podTemplate(label: label, serviceAccount: 'jenkins', containers: [
        	[name: 'maven', image: 'fabric8/maven-builder', command: 'cat', ttyEnabled: true, 
        	envVars: [                
                		[key: 'DOCKER_CONFIG', value:'/home/jenkins/.docker/'],
               		 ],
	
        	[name: 'jnlp', image: 'iocanel/jenkins-jnlp-client:latest', command:'/usr/local/bin/start.sh', args: '${computer.jnlpmac} ${computer.name}', ttyEnabled: false,
                envVars: [
                	[key: 'DOCKER_HOST', value: 'unix:/var/run/docker.sock']
                		]
                	]
                	],
        	volumes: [
                [$class: 'PersistentVolumeClaim', mountPath: '/home/jenkins/.mvnrepository', claimName: 'jenkins-mvn-local-repo'],
                [$class: 'SecretVolume', mountPath: '/home/jenkins/.m2/', secretName: 'jenkins-maven-settings'],
                [$class: 'SecretVolume', mountPath: '/home/jenkins/.docker', secretName: 'jenkins-docker-cfg'],
                [$class: 'HostPathVolume', mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock']
        ]])


  kubernetes.pod('buildpod')
  .withNewContainer()
  	.withImage('jhipster/jhipster')  	
      .withPrivileged(true)
      //.withHostPathMount('/var/run/docker.sock','/var/run/docker.sock')
      //.withEnvVar('DOCKER_CONFIG','/home/jenkins/.docker/')
      //.withSecret('jenkins-docker-cfg','/home/jenkins/.docker')
      //.withSecret('jenkins-maven-settings','/root/.m2')
      //.withServiceAccount('jenkins')
      .inside {
	
    stage 'Canary Release'
    mavenCanaryRelease{
      version = canaryVersion
    }

    stage 'Integration Test'
    mavenIntegrationTest{
      environment = 'Testing'
      failIfNoTests = localFailIfNoTests
      itestPattern = localItestPattern
    }

    stage 'Rolling Upgrade Production'
    def rc = readFile 'target/classes/kubernetes.json'
    kubernetesApply(file: rc, environment: envProd)

  }
}

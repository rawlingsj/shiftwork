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




  kubernetes.pod('buildpod')
  //.withHostPathMount('/var/run/docker.sock','/var/run/docker.sock')
  .withSecret('jenkins-docker-cfg','/home/jenkins/.docker')
  .withSecret('jenkins-maven-settings','/root/.m2')
  .withServiceAccount('jenkins')
  .withNewContainer()
  	.withImage('jhipster/jhipster')  	
      .withPrivileged(true)
      .withEnvVar('DOCKER_CONFIG',  '/home/jenkins/.docker/'	)      
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

#!/usr/bin/groovy
@Library('github.com/fabric8io/fabric8-pipeline-library@master')
def buildLabel = "mylabel.${env.JOB_NAME}.${env.BUILD_NUMBER}".replace('-', '_').replace('/', '_')

podTemplate(label: buildLabel, 
 containers: [containerTemplate(image: 'maven', name: 'maven', command: 'cat', ttyEnabled: true,
        envVars: [
        	containerEnvVar(
        		key: 'DOCKER_CONFIG', 
        		value: '/home/jenkins/.docker/')], 
        	image:( 'jhipster/jhipster', name: 'maven', privileged: true, resourceLimitCpu: '', resourceLimitMemory: '', resourceRequestCpu: '', resourceRequestMemory: '', ttyEnabled: true, workingDir: '/home/jenkins'),
        	workingDir: '/home/jenkins/')
        ],
 volumes: [
        hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock'), 
        secretVolume(mountPath: '/root/.m2', secretName: 'jenkins-maven-settings'), 
      	secretVolume(mountPath: '/home/jenkins/.docker', secretName: 'jenkins-docker-cfg')
    ],
 serviceAccount: 'jenkins') {
    node(buildLabel) {
        container(name: 'maven') {
            def envProd = 'shiftwork-production'

		  checkout scm
		
		
		
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
 }
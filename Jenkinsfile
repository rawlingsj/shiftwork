#!/usr/bin/groovy
@Library('github.com/fabric8io/fabric8-pipeline-library@master')
def buildLabel = "mylabel.${env.JOB_NAME}.${env.BUILD_NUMBER}".replace('-', '_').replace('/', '_')

podTemplate(label: buildLabel, 
 containers: [containerTemplate(image: 'maven', name: 'maven', command: 'cat', ttyEnabled: true,
        envVars: [containerEnvVar(key: 'DOCKER_CONFIG', value: '/home/jenkins/.docker/')], 
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
            sh 'pwd'
            sh 'whoami'
            sh 'echo hello world'
        }
    }
 }
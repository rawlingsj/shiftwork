@Library('github.com/fabric8io/fabric8-pipeline-library@master')
def dummy
dockerTemplate{
  mavenNode(mavenImage: 'jhipster/jhipster') {

    container(name: 'maven') {
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
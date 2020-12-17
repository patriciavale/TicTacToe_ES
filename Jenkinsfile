pipeline {
  agent any
  stages {
    stage('Checkout'){
        steps{
            checkout scm
        }
    }
    stage('Build/Package'){
      steps{
        parallel(
          eureka_server: {
            sh 'mvn -f eureka_server/ package'
          },
          data_proxy: {
            sh 'mvn -f data_proxy/ package'
          },
          action_decoder: {
            sh 'mvn -f action_decoder/ package'
          },
          game_manager: {
            sh 'mvn -f game_manager/ package'
          },
          web_server: {
            sh 'mvn -f web_server/ package'
          }
        )
      }
    }  
    stage('Push to artifactory'){
      environment{
        ARTIFACTORY_CREDS = credentials('artifactory')
      }
      steps{
        sh "curl -u $ARTIFACTORY_CREDS -X PUT \"http://192.168.160.86:8081/artifactory/p2g2/eureka/eureka.jar\" -T \$(ls eureka_server/target/*.jar)"
        sh "curl -u $ARTIFACTORY_CREDS -X PUT \"http://192.168.160.86:8081/artifactory/p2g2/data_proxy/data_proxy.jar\" -T \$(ls data_proxy/target/*.jar)"
        sh "curl -u $ARTIFACTORY_CREDS -X PUT \"http://192.168.160.86:8081/artifactory/p2g2/action_decoder/action_decoder.jar\" -T \$(ls data_proxy/target/*.jar)"
        sh "curl -u $ARTIFACTORY_CREDS -X PUT \"http://192.168.160.86:8081/artifactory/p2g2/game_manager/game_manager.jar\" -T \$(ls game_manager/target/*.jar)"
        sh "curl -u $ARTIFACTORY_CREDS -X PUT \"http://192.168.160.86:8081/artifactory/p2g2/web_server/web_server.jar\" -T \$(ls web_server/target/*.jar)"
      }
    }
    stage('Build/Push Docker'){
      steps{
        parallel (
          eureka_server: {
            sh "docker build -t 192.168.160.86:5000/p2g2_eureka eureka_server/"
            sh "docker push 192.168.160.86:5000/p2g2_eureka"
          },
          data_proxy: {
            sh "docker build -t 192.168.160.86:5000/p2g2_proxy data_proxy/"
            sh "docker push 192.168.160.86:5000/p2g2_proxy"
          },
          action_decoder: {
            sh "docker build -t 192.168.160.86:5000/p2g2_decoder action_decoder/"
            sh "docker push 192.168.160.86:5000/p2g2_decoder"
          },
          game_manager: {
            sh "docker build -t 192.168.160.86:5000/p2g2_manager game_manager/"
            sh "docker push 192.168.160.86:5000/p2g2_manager"
          },
          web_server: {
            sh "docker build -t 192.168.160.86:5000/p2g2_web web_server/"
            sh "docker push 192.168.160.86:5000/p2g2_web"
          }
        )
      }
    }
    stage('Deploy'){
      environment{
        PASS = credentials('vmp2g2')
      }
      steps{
        script{
          remote = [:]
          remote.name = "192.168.160.80"
          remote.host = "192.168.160.80"
          remote.allowAnyHosts = true
          remote.user = "p2g2"
          remote.password = PASS
        }
        sshCommand remote: remote, command: 'docker stop p2g2_eureka || true'
        sshCommand remote: remote, command: 'docker stop p2g2_dataproxy || true'
        sshCommand remote: remote, command: 'docker stop p2g2_decoder || true'
        sshCommand remote: remote, command: 'docker stop p2g2_manager || true'
        sshCommand remote: remote, command: 'docker stop p2g2_web || true'
        sshCommand remote: remote, command: 'docker rmi 192.168.160.86:5000/p2g2_eureka || true'
        sshCommand remote: remote, command: 'docker rmi 192.168.160.86:5000/p2g2_proxy || true'
        sshCommand remote: remote, command: 'docker rmi 192.168.160.86:5000/p2g2_decoder || true'
        sshCommand remote: remote, command: 'docker rmi 192.168.160.86:5000/p2g2_manager || true'
        sshCommand remote: remote, command: 'docker rmi 192.168.160.86:5000/p2g2_web || true'
        sleep(5)
        sshCommand remote: remote, command: 'docker run --name p2g2_eureka -p 8250:8250 -e HOST_IP=192.168.160.80 -e KAFKA_URL=192.168.160.80:39092 --rm -d 192.168.160.86:5000/p2g2_eureka'
        sleep(5)
        sshCommand remote: remote, command: 'docker run --name p2g2_dataproxy -p 8251:8251 -e EUREKA_URL=192.168.160.80:8250 -e HOST_IP=192.168.160.80 -e KAFKA_URL=192.168.160.80:39092 --rm -d 192.168.160.86:5000/p2g2_proxy'
        sshCommand remote: remote, command: 'docker run --name p2g2_decoder -p 8252:8252 -e EUREKA_URL=192.168.160.80:8250 -e HOST_IP=192.168.160.80 -e KAFKA_URL=192.168.160.80:39092 --rm -d 192.168.160.86:5000/p2g2_decoder'
        sshCommand remote: remote, command: 'docker run --name p2g2_manager -p 8253:8253 -e EUREKA_URL=192.168.160.80:8250 -e HOST_IP=192.168.160.80 -e KAFKA_URL=192.168.160.80:39092 --rm -d 192.168.160.86:5000/p2g2_manager'
        sshCommand remote: remote, command: 'docker run --name p2g2_web -p 8254:8254 -e EUREKA_URL=192.168.160.80:8250 -e HOST_IP=192.168.160.80 -e KAFKA_URL=192.168.160.80:39092 --rm -d 192.168.160.86:5000/p2g2_web'
      }
    }
  }
}

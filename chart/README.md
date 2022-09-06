## kic-spring-template

## 배포
```
helm install kic-spring-template -f values-dev.yaml .
```

## 삭제
```
helm delete kic-spring-template
```

## Values
| Key | Description | Default |
|---|:---|:---|
| `namespace` | k8s 네임스페이스 | `default` |
| `app.name` | k8s resource 이름 | `container-registry` |
| `app.profile` | active spring profile | `` |
| `app.replicaCount` | replica 갯수 | `1` |
| `app.hostAliases` | hosts 설정 | `[]` |
| `app.image` | 사용할 이미지 path | `idock.daumkakao.io/cloud-service/sample:latest` |
| `app.resources` | 리소스 설정 | `{}` |
| `app.service.type` | 서비스 타입 | `NodePort` |
| `app.service.nodePort` | 서비스 타입이 NodePort 일 경우 사용 | `` |
| `app.ingress.enabled` | ingress 사용 여부 | `true` |
| `app.ingress.annotations` | ingress annotations | `{}` |
| `app.ingress.hosts` | ingress host 정보 | `[]` |
| `app.ingress.tls.enabled` | ingress tls 사용여부 | `false` |
| `app.ingress.tls.secretName` | ingress tls 시크릿 | `` |
 
### Override values
| File | Description |
|---|:---|
| `values-dev.yaml` | dev |
| `values-stg.yaml` | stg |
| `values-prod.yaml` | prod |

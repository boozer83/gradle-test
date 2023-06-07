## CR Fluentd

## 빌드
```
docker build -t idock.daumkakao.io/cloud-service/cr-fluentd:v1.15-debian-mongo-1 .
```

## 배포
```
helm install fluentd -f values.yaml -n monitoring .
```

## 삭제
```
helm delete fluentd -n monitoring
```

## Values
| Key | Description | Default |
|---|:---|:---|
| `namespace` | k8s 네임스페이스 | `default` |
 
### Override values
| File | Description |
|---|:---|
| `values-stg.yaml` | stg |
| `values-prod.yaml` | prod |

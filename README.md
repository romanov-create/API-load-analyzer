# API load analyzer app
## Technolodgy stack: Spring Boot, Spring MVC, Swagger

## How to start:
1. run maven goal 'mvn clean install'
2. run ApiLoadAnalyzerApplication
3. in /resources has file test.csv (you can use for test)

### Simple response (Json format) :
```json
{
    "resultEntries": [
        {
            "uri": "/save/",
            "method": "PUT",
            "count": 2
        },
        {
            "uri": "/",
            "method": "GET",
            "count": 1
        }
    ],
    "statisticsEntries": [
        {
            "key": "28/07/2006:10:25:04-0300",
            "value": 2
        },
        {
            "key": "28/07/2006:10:22:04-0300",
            "value": 1
        }
    ],
    "counters": {
        "totalRows": 4,
        "validRows": 3,
        "processedTime": 2
    }
}
```

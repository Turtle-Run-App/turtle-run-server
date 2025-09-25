curl -X POST http://localhost:8081/api/bulkInitial \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1004,
    "syncStartTime": "2025-09-15T00:00:00",
    "syncEndTime": "2025-09-15T10:30:00",
    "runningSessions": [
      {
        "workoutId": "workout-uuid-001",
        "startTime": "2025-09-10T07:00:00",
        "endTime": "2024-09-10T07:30:00",
        "workoutType": "running",
        "distance": 5000.0,
        "duration": 1800,
        "calories": 350,
        "avgHeartRate": 150,
        "route": [
          {
            "latitude": 37.5665,
            "longitude": 126.9780,
            "timestamp": "2024-01-10T07:00:00"
          },
          {
            "latitude": 37.5666,
            "longitude": 126.9781,
            "timestamp": "2024-01-10T07:30:00"
          }
        ]
      }
    ]
  }'
using UnityEngine;

namespace Assets.Gamelogic.Visualizers.Player
{
    public class RtsCameraController : MonoBehaviour
    {
        public Transform CameraTransform;
        public RectTransform MiniMapRectTransform;
        public bool MouseMovementOn = false;
        public float MinDistance = 0f;
        public float MaxDistance = 10f;
        public float Distance = 2f;
        public float Rotation = 0f;
        public float MovementSpeed = 2f;
        public float EdgeSensitivity = 0.2f;

        void OnEnable()
        {
            CameraTransform = GetComponentInChildren<Camera>().transform;
            UpdateDistance(Distance);
            GameObject miniMap = GameObject.Find("MiniMap");
            if (miniMap) MiniMapRectTransform = miniMap.GetComponent<RectTransform>();
        }

        void Update()
        {
            if (Input.GetMouseButton(1))
            {
                UpdateRotation(Rotation + Input.GetAxis("Mouse X")*5);
            }
            UpdatePosition();
            Distance = Mathf.Clamp(Distance - Input.GetAxis("Mouse ScrollWheel")*Distance, MinDistance, MaxDistance);
            UpdateDistance(Distance);
        }

        private void UpdatePosition()
        {
            Vector3 movementDirection = new Vector3(Input.GetAxis("Horizontal"), 0.0f, Input.GetAxis("Vertical"));
            float actualMovementSpeed = (Input.GetKey(KeyCode.LeftShift)) ? MovementSpeed*4f : MovementSpeed;
            transform.position += transform.rotation*movementDirection*actualMovementSpeed*Distance*Time.deltaTime;

            if (MouseMovementOn)
            {
                if (Input.mousePosition.x <= Screen.width*EdgeSensitivity && Input.mousePosition.x >= 0.0f)
                {
                    transform.position += transform.rotation*
                                          new Vector3(-actualMovementSpeed*Distance*Time.deltaTime, 0.0f, 0.0f);
                }

                if (Input.mousePosition.x >= Screen.width*(1.0f - EdgeSensitivity) &&
                    Input.mousePosition.x <= Screen.width)
                {
                    transform.position += transform.rotation*
                                          new Vector3(actualMovementSpeed*Distance*Time.deltaTime, 0.0f, 0.0f);
                }

                if (Input.mousePosition.y <= Screen.height*EdgeSensitivity && Input.mousePosition.y >= 0.0f)
                {
                    transform.position += transform.rotation*
                                          new Vector3(0.0f, 0.0f, -actualMovementSpeed*Distance*Time.deltaTime);
                }

                if (Input.mousePosition.y >= Screen.height*(1.0f - EdgeSensitivity) &&
                    Input.mousePosition.y <= Screen.height)
                {
                    transform.position += transform.rotation*
                                          new Vector3(0.0f, 0.0f, actualMovementSpeed*Distance*Time.deltaTime);
                }
            }
        }

        private void UpdateRotation(float r)
        {
            Rotation = r;
            transform.rotation = Quaternion.AngleAxis(r, new Vector3(0.0f, 1.0f, 0.0f));
        }

        private void UpdateDistance(float d)
        {
            CameraTransform.localPosition = new Vector3(CameraTransform.localPosition.x, d,CameraTransform.localPosition.z);
        }
    }
}
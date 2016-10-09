using UnityEngine;

namespace Assets.Gamelogic.Visualizers.Util
{
    public class HorizontalRotation : MonoBehaviour
    {
        public float TimeCounter;
        public float RotationSpeed = 80.0f;

        void Update()
        {
            TimeCounter += Time.deltaTime;
            transform.localPosition = new Vector3(transform.localPosition.x, (Mathf.Sin(TimeCounter) + 1f) * 6f + 2f, transform.localPosition.z);
            transform.Rotate(Vector3.up * RotationSpeed * Time.deltaTime);
        }
    }
}

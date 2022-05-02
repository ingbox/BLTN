from sklearn.cluster import KMeans
import numpy as np
import cv2
import matplotlib.pyplot as plt
from sklearn.utils import shuffle

img = cv2.imread("images/angry_pearl_denoised_noback.jpg")
img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB) # imread의 rgb 순서가 달라 다시 rgb로 바꾸어줌
img = np.array(img, dtype=np.float64) / 255 # RGB 0~1로 수렴
w, h, d = original_shape = tuple(img.shape) # 440 660 3

assert d == 3
image_array = np.reshape(img, (w * h, d)) #원래의 이미지 shape를 2차원으로 변환
print(image_array.shape)

image_array_sample = shuffle(image_array, random_state=0)[:1000]
kmeans = KMeans(n_clusters=36, random_state=0).fit(image_array_sample) # k = n  로 클러스터링 할 모델 만든 후 변환

labels = kmeans.predict(image_array)
print(labels) # 각 색상을 군집화하여 하나의 픽셀마다 RGB 값을 매칭해 놓은 것

def recreate_image(codebook, labels, w, h):
    """예측한 label 값을 넣어줘서 클러스터된 이미지를 만듦"""
    d = codebook.shape[1]
    image = np.zeros((w, h, d))
    label_idx = 0
    for i in range(w):
        for j in range(h):
            image[i][j] = codebook[labels[label_idx]]
            label_idx += 1
    return image

plt.clf()
plt.axis('off')
plt.imshow(recreate_image(kmeans.cluster_centers_, labels, w, h))
plt.savefig('images/angry_pearl_clustered_noback36.jpg',bbox_inches = 'tight', pad_inches = 0)
plt.show()






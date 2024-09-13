import numpy as np
import torch
from transformers import BertTokenizer, BertForSequenceClassification
import random
import sys
import logging


# 랜덤 시드 설정
def set_seed(seed):
    random.seed(seed)
    np.random.seed(seed)
    torch.manual_seed(seed)
    torch.cuda.manual_seed_all(seed)
    torch.backends.cudnn.deterministic = True
    torch.backends.cudnn.benchmark = False

seed = 42
set_seed(seed)

# 라벨 인코더 딕셔너리
label_encoder = {
    0: '간식',
    1: '강연',
    2: '건강',
    3: '공모전',
    4: '교육',
    5: '기타',
    6: '대회',
    7: '동아리',
    8: '선거',
    9: '연구',
    10: '인턴',
    11: '장학',
    12: '제휴',
    13: '졸업',
    14: '축제',
    15: '취업',
    16: '친목',
    17: '해외',
    18: '행사'
}

# 모델과 토크나이저 로드 (절대 경로 사용)
model_path = "G:\\dataPredict\\saved_model"
tokenizer = BertTokenizer.from_pretrained(model_path)
model = BertForSequenceClassification.from_pretrained(model_path)

# 디바이스 설정
device = torch.device('cuda') if torch.cuda.is_available() else torch.device('cpu')
model.to(device)
model.eval()

def extract_keywords(text):
    # 텍스트를 토큰화
    batch = tokenizer.encode_plus(
        text,
        add_special_tokens=True,
        max_length=512,
        return_token_type_ids=True,
        padding='max_length',
        return_attention_mask=True,
        return_tensors='pt',
        truncation=True
    )

    with torch.no_grad():
        input_ids = batch['input_ids'].long().to(device)
        attention_mask = batch['attention_mask'].long().to(device)
        token_type_ids = batch['token_type_ids'].long().to(device)

        outputs = model(input_ids, attention_mask=attention_mask)
        logits = outputs.logits
        preds = logits.argmax(dim=1).cpu().item()

    keywords = label_encoder[preds]
    return keywords

if __name__ == "__main__":
    input_text = sys.argv[1]
    keywords = extract_keywords(input_text)
    print(f'{keywords}')

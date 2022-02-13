import json

from rest_framework.decorators import api_view
from django.http import HttpResponse

from todo.data_store import item_store as store
from todo.serializers import UUIDEncoder, UUIDDecoder, ItemSerializer


@api_view(['GET', 'DELETE'])
def handle_item(request, item_id):
    if request.method == 'GET':
        return get_item_by_pk(request, item_id)
    if request.method == 'DELETE':
        return delete_item(request, item_id)
    return HttpResponse(status=204)


def get_item_by_pk(request, item):
    try:
        item = store.get_item(item)
        result = ItemSerializer().serialize(item)
        return HttpResponse(json.dumps(result, cls=UUIDEncoder), content_type='application/json')
    except Exception as e:
        print(e)
        return HttpResponse(status=404)


def delete_item(request, item):
    if store.delete_item(item):
        return HttpResponse()
    else:
        return HttpResponse(status=404)


@api_view(['GET', 'POST'])
def handle_items(request):
    if request.method == 'GET':
        return get_items(request)
    if request.method == 'POST':
        return create_item(request)
    if request.method == 'PUT':
        return update_item(request)
    return HttpResponse(204)


def get_items(request):
    items = store.get_items()
    result = [ItemSerializer().serialize(entry) for entry in items]
    return HttpResponse(json.dumps(result, cls=UUIDEncoder), content_type='application/json')


def create_item(request):
    serializer = ItemSerializer()
    obj = serializer.deserialize(request.body)
    item = store.create_item(obj)
    result = serializer.serialize(item)
    return HttpResponse(json.dumps(result, cls=UUIDEncoder), content_type='application/json')


def update_item(request):
    obj = ItemSerializer().deserialize(request.body)
    if store.update_item(obj):
        return HttpResponse(True)
    else:
        return HttpResponse(status=404)


@api_view(['GET'])
def get_items_for_agent(request, agent_id):
    items = store.get_items_for_agent(agent_id)
    result = [ItemSerializer().serialize(entry) for entry in items]
    return HttpResponse(json.dumps(result, cls=UUIDEncoder), content_type='application/json')


@api_view(['GET'])
def get_item_by_title(request, title):
    item = store.get_item_by_title(title)
    if item is None:
        return HttpResponse(status=404)
    result = ItemSerializer().serialize(item)
    return HttpResponse(json.dumps(result), content_type='application/json')
